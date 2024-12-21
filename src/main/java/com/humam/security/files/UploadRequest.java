package com.humam.security.files;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadRequest {
    
    @NotNull(message = "File must not be null")
    private MultipartFile file;

    @NotNull(message = "GroupID is required")
    private Integer groupId;
}
