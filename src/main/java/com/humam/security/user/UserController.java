package com.humam.security.user;

import com.humam.security.utils.GenericResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

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

}
