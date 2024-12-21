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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(
        @RequestHeader("Authorization") String token,
        @Valid @ModelAttribute UploadRequest request
    ) {
        try {
            String message = fileService.uploadFile(token, request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFile(
            @RequestHeader("Authorization") String token,
            @PathVariable @NotNull @Min(1) Integer id,
            @RequestParam("file") @NotNull MultipartFile file
    ) throws IOException {
        String message = fileService.updateFile(id, file);
        return ResponseEntity.ok(message);
    }


    @PutMapping("/accept/{id}")
    public ResponseEntity<String> acceptFile(
            @RequestHeader("Authorization") String token,
            @PathVariable @NotNull @Min(1) Integer id
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
    public ResponseEntity<Resource> handleFileDownload(
        @RequestHeader("Authorization") String token,
        @RequestParam("fileId") Integer fileId
        ) {
        try {
            Resource file = fileService.downloadFile(fileId, token);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/downloadmany")
    public ResponseEntity<Resource> handleMultiFileDownload(
        @RequestHeader("Authorization") String token,
        @RequestParam("fileIds") List<Integer> fileIds
    ) {
        try {
            Resource zipFile = fileService.processAndPrepareZip(fileIds, token);
        
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipFile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
