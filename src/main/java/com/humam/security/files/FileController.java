package com.humam.security.files;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.humam.security.utils.GenericResponse;

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

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<GenericResponse<String>> handleFileUpload(
            @RequestHeader("Authorization") String token,
            @Valid @ModelAttribute UploadRequest request
    ) throws IOException {
        String message = fileService.uploadFile(token, request);
        return ResponseEntity.ok(GenericResponse.success(message, "File uploaded successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse<String>> updateFile(
            @RequestHeader("Authorization") String token,
            @PathVariable @NotNull @Min(1) Integer id,
            @RequestParam("file") @NotNull MultipartFile file
    ) throws IOException {
        String message = fileService.updateFile(id, file);
        return ResponseEntity.ok(GenericResponse.success(message, "File updated successfully"));
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<GenericResponse<String>> acceptFile(
            @RequestHeader("Authorization") String token,
            @PathVariable @NotNull @Min(1) Integer id
    ) throws IOException {
        String message = fileService.acceptFile(id);
        return ResponseEntity.ok(GenericResponse.success(message, "File accepted successfully"));
    }

    @DeleteMapping("/reject/{id}")
    public ResponseEntity<GenericResponse<String>> rejectFile(
            @RequestHeader("Authorization") String token,
            @PathVariable @NotNull @Min(1) Integer id
    ) throws IOException {
        String message = fileService.rejectFile(id);
        return ResponseEntity.ok(GenericResponse.success(message, "File rejected successfully"));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> handleFileDownload(
            @RequestHeader("Authorization") String token,
            @PathVariable @NotNull @Min(1) Integer id
    ) throws IOException {
        Resource file = fileService.downloadFile(id, token);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/downloadmany")
    public ResponseEntity<Resource> handleMultiFileDownload(
            @RequestHeader("Authorization") String token,
            @RequestParam("fileIds") @NotNull List<@Min(1) Integer> fileIds
    ) throws IOException {
        Resource zipFile = fileService.processAndPrepareZip(fileIds, token);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipFile);
    }
}
