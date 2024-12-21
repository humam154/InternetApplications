package com.humam.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping("/change")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    )
    {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateProfileRequest request,
            Principal connectedUser
    )
    {
        service.updateProfile(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
