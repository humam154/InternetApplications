package com.humam.security.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.humam.security.file.FileData;
import com.humam.security.file.FileRepository;
import com.humam.security.group.Group;
import com.humam.security.group.GroupRepository;
import com.humam.security.user.User;
import com.humam.security.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository repository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public String uploadFile(MultipartFile file, Integer userId, Integer groupId) throws IOException {
        String folderPath = System.getProperty("user.dir") + "/public" + File.separator;

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        Path targetPath = Paths.get(folderPath).normalize();
        Files.createDirectories(targetPath);
        
        Path filePath = targetPath.resolve(file.getOriginalFilename());
        file.transferTo(filePath.toFile());

        FileData fileData = repository.save(FileData.builder()
            .name(file.getOriginalFilename())
            .file(filePath.toString())
            .createdBy(user)
            .group(group)
            .build());

        return "File uploaded successfully: " + fileData.getName();
    }
}
