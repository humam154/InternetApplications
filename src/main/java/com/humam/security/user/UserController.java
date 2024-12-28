package com.humam.security.user;

import com.humam.security.utils.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping("/change")
    public ResponseEntity<GenericResponse<ChangePasswordResponse>> changePassword(
            @RequestBody ChangePasswordRequest request,
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
}
