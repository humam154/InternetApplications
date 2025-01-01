package com.humam.security.files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.humam.security.file.FileCheck;
import com.humam.security.file.FileCheckRepository;
import com.humam.security.file.FileData;
import com.humam.security.file.FileRepository;
import com.humam.security.group.Group;
import com.humam.security.group.GroupRepository;
import com.humam.security.group.GroupService;
import com.humam.security.token.TokenRepository;
import com.humam.security.user.User;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository repository;
    private final FileCheckRepository fileCheckRepository;
    private final TokenRepository tokenRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;

    public String uploadFile(String token, UploadRequest request) throws IOException {
        String folderPath = System.getProperty("user.dir") + "/public" + File.separator;

        MultipartFile file = request.getFile();
        
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        token = token.replaceFirst("^Bearer ", "");
        User user = tokenRepository.findByToken(token).get().getUser();

        Group group = groupRepository.findById(request.getGroupId())
            .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        Path targetPath = Paths.get(folderPath).normalize();
        Files.createDirectories(targetPath);

        Path filePath = targetPath.resolve(file.getOriginalFilename());
        file.transferTo(filePath.toFile());

        FileData fileData = repository.save(FileData.builder()
            .name(file.getOriginalFilename())
            .filePath(filePath.toString())
            .createdBy(user)
            .group(group)
            .accepted(group.getCreatedBy().equals(user))
            .inUse(false)
            .version(0)
            .build());

        return "File uploaded successfully: " + fileData.getName();
    }

    @Transactional
    public String updateFile(Integer fileId, MultipartFile multipartFile) throws IOException {
       

        FileData existingFile = repository.findByIdForUpdate(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + fileId));

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Path filePath = Path.of(existingFile.getFilePath());
        Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        existingFile.setVersion(existingFile.getVersion()+1);
        repository.save(existingFile);
        changeFileStatus(existingFile);
        return "File updated successfully: " + existingFile.getName();
    }

    public String acceptFile(Integer fileId){
        FileData fileData = repository.findById(fileId)
        .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + fileId));

        fileData.setAccepted(true);

        return "Accepted file successfully!";
    }

    public String rejectFile(Integer fileId){
        FileData fileData = repository.findById(fileId)
        .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + fileId));

        repository.delete(fileData);

        return "Rejected file and deleted successfully!";
    }

    // a switch to update file status, useful for buttons
    public void changeFileStatus(FileData file){
        file.setInUse(!file.getInUse());
        repository.save(file);
    }

    @Transactional
    public Resource downloadFile(Integer id, String token) throws IOException {

        FileData fileData = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("File not found"));

        if(fileData.getInUse()){
            throw new StorageException("File already in use");
        }

        token = token.replaceFirst("^Bearer ", "");
        User user = tokenRepository.findByToken(token).get().getUser();

        FileCheck fileCheck = fileCheckRepository.save(FileCheck.builder()
        .checkedBy(user)
        .fileId(fileData)
        .checkDate(Instant.now())
        .build());
        changeFileStatus(fileData);

        Path filePath = Paths.get(fileData.getFilePath()).toAbsolutePath();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new StorageException("Could not read file: " + filePath.toString());
        }
    }


    @Transactional
    public Resource processAndPrepareZip(List<Integer> fileIds, String token) throws IOException {
        
        token = token.replaceFirst("^Bearer ", "");
        User user = tokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"))
            .getUser();

        List<FileData> files = repository.findAllNotInUseByIds(fileIds);

        if (files.size() != fileIds.size()) {
            throw new IllegalArgumentException("Some files are currently in use.");
        }

        for(FileData file : files){
            changeFileStatus(file);

            fileCheckRepository.save(FileCheck.builder()
            .checkedBy(user)
            .fileId(file)
            .build());

        }

        return createZipFromFiles(files);
    }
    private Boolean isFileCheckedInByUser(User user, FileData file) {
        FileCheck check = fileCheckRepository.findLastCheckByFile(file.getId())
            .orElseThrow(() -> new NoSuchElementException("No check found for file with ID: " + file.getId()));
    
        if (check.getCheckedBy() == null) {
            return false;
        }
    
        return check.getCheckedBy().getId().equals(user.getId());
    }
    

    public List<FileDataResponse> groupFiles(String token, Integer gid) {
        List<FileData> files;
        boolean isGroupOwner = groupService.isGroupOwner(token, gid);
        if(isGroupOwner) {
            files = repository.findByGroupId(gid);
        }
         else {
            files = repository.findByGroupIdAndAcceptedTrue(gid);
        }
        token = token.replaceFirst("^Bearer ", "");
        User user = tokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"))
            .getUser();
    
        return files.stream().map(file -> FileDataResponse.builder()
            .id(file.getId())
            .name(file.getName())
            .groupName(file.getGroup().getName())
            .createdByUser(file.getCreatedBy().getFirst_name() + " " + file.getCreatedBy().getLast_name())
            .isOwner(file.getCreatedBy().getId() == user.getId() ? true : false)
            .isGroupOwner(isGroupOwner)
            .checkedInByCurrentUser(isFileCheckedInByUser(user, file))
            .accepted(file.getAccepted())
            .inUse(file.getInUse())
            .version(1)
            .build()
        ).toList();
    }
    
    public List<FileDataResponse> pendingFiles(String token, Integer gid) {
        List<FileData> files;
        boolean isGroupOwner = groupService.isGroupOwner(token, gid);
        if(isGroupOwner) {
            files = repository.findByGroupIdAndAcceptedFalse(gid);
        }
         else {
            throw new IllegalStateException("You are not the group owner");
        }

        return files.stream().map(file -> FileDataResponse.builder()
            .id(file.getId())
            .name(file.getName())
            .groupName(file.getGroup().getName())
            .createdByUser(file.getCreatedBy().getFirst_name() + " " + file.getCreatedBy().getLast_name())
            .isGroupOwner(isGroupOwner)
            .accepted(file.getAccepted())
            .inUse(file.getInUse())
            .version(1)
            .build()
        ).toList();
    }

    private ByteArrayResource createZipFromFiles(List<FileData> files) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            for (FileData file : files) {
                Path filePath = Paths.get(file.getFilePath());
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                Files.copy(filePath, zipOutputStream);
                zipOutputStream.closeEntry();
            }
        }
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }
}