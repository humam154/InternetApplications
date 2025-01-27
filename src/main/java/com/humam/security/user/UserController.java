package com.humam.security.user;

import com.humam.security.report.ReportService;
import com.humam.security.utils.GenericResponse;

import com.itextpdf.text.DocumentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final ReportService reportService;

    @GetMapping("/profile")
    public ResponseEntity<GenericResponse<UpdateProfileResponse>> getProfile(
            Principal connectedUser
    ) 
    {
        UpdateProfileResponse response = service.getProfile(connectedUser);
        return ResponseEntity.ok(GenericResponse.success(response, "Profile fetched successfully!"));
    }
    
    @PatchMapping("/change")
    public ResponseEntity<GenericResponse<ChangePasswordResponse>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    )
    {
        var response = service.changePassword(request, connectedUser);
        return ResponseEntity.ok(GenericResponse.success(response, "Password changed successfully!"));
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse<UpdateProfileResponse>> updateProfile(
            @RequestBody UpdateProfileRequest request,
            Principal connectedUser
    )
    {
        var response = service.updateProfile(request, connectedUser);
        return ResponseEntity.ok(GenericResponse.success(response, "Profile updated!"));
    }

    @GetMapping("/allusers")
    public ResponseEntity<GenericResponse<List<SearchUserResponse>>> getAllUsers(
        @RequestParam String query
        ) 
        {
            List<SearchUserResponse> users = service.searchUsers(query);
            return ResponseEntity.ok(GenericResponse.success(users, "success"));
    }
    
    @GetMapping("/search")
    public ResponseEntity<GenericResponse<List<SearchUserResponse>>> searchUsersInGroup(
            @RequestParam String query,
            @RequestParam boolean isMember,
            @RequestParam Integer groupId
    ) {
        List<SearchUserResponse> users;
        try {
            users = service.searchUsersInGroup(query, isMember,groupId);
            return ResponseEntity.ok(GenericResponse.success(users, "success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(GenericResponse.error(e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/most-active-user")
    public ResponseEntity<byte[]> downloadMostActiveUserReport() throws IOException, DocumentException {
        byte[] pdfBytes = reportService.generateMostActiveUserReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "most_active_user_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/most-active-group")
    public ResponseEntity<byte[]> generateAndSaveMostActiveGroupReport(
            @RequestParam(required = false) String filePath) throws IOException, DocumentException {
        byte[] pdfBytes = reportService.generateMostActiveGroupReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "most_active_user_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }}
