package com.humam.security.files;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(
        @RequestHeader("Authorization") String token,
        @RequestParam("file") MultipartFile file,
        @RequestParam("groupId") Integer groupId
    ) {
        try {
            String message = fileService.uploadFile(file, token, groupId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFile(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String message = fileService.updateFile(id, file, token);
        return ResponseEntity.ok(message);
    }


    @PutMapping("/accept/{id}")
    public ResponseEntity<String> acceptFile(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id
    ) throws IOException {
        String message = fileService.acceptFile(id);
        return ResponseEntity.ok(message);
    }


    @PutMapping("/reject/{id}")
    public ResponseEntity<String> rejectFile(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id
    ) throws IOException {
        String message = fileService.rejectFile(id);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> handleFileDownload(@RequestParam("fileId") Integer fileId) {
        try {
            Resource file = fileService.downloadFile(fileId);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
