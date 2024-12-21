package com.humam.security.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.humam.security.file.FileCheck;
import com.humam.security.file.FileCheckRepository;
import com.humam.security.file.FileData;
import com.humam.security.file.FileRepository;
import com.humam.security.group.Group;
import com.humam.security.group.GroupRepository;
import com.humam.security.token.TokenRepository;
import com.humam.security.user.User;
import com.humam.security.user.UserRepository;

import com.humam.security.files.UploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository repository;
    private final FileCheckRepository fileCheckRepository;
    private final TokenRepository tokenRepository;
    private final GroupRepository groupRepository;

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
            .accepted(false)
            .in_use(false)
            .build());

        return "File uploaded successfully: " + fileData.getName();
    }

    public String updateFile(Integer fileId, MultipartFile multipartFile, String token) throws IOException {
       

        FileData existingFile = repository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + fileId));
        
        token = token.replaceFirst("^Bearer ", "");
        User user = tokenRepository.findByToken(token).get().getUser();

        FileCheck fileCheck = fileCheckRepository.save(FileCheck.builder()
        .checkedBy(user)
        .fileId(existingFile)
        .build());

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Path filePath = Path.of(existingFile.getFilePath());
        Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // when a user updates a file, it's unlocked (discussion needed)
        existingFile.setIn_use(false);
        repository.save(existingFile);
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
        file.setIn_use(!file.getIn_use());
        repository.save(file);
    }

    public Resource downloadFile(Integer id) throws IOException {
        FileData fileData = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("File not found"));

        if(fileData.getIn_use()){
            throw new StorageException("File already in use");
        }

        // when a user downloads a file, it's locked (discussion needed)
        fileData.setIn_use(true);
        repository.save(fileData);

        Path filePath = Paths.get(fileData.getFilePath()).toAbsolutePath();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new StorageException("Could not read file: " + filePath.toString());
        }
    }
}