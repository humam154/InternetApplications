package com.humam.security.user;

import com.humam.security.utils.ResponseUtil;
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
    public ResponseEntity<Object> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    )
    {
        var response = service.changePassword(request, connectedUser);
        return ResponseUtil.success("password changed", response);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateProfile(
            @RequestBody UpdateProfileRequest request,
            Principal connectedUser
    )
    {
        var response = service.updateProfile(request, connectedUser);
        return ResponseUtil.success("profile updated", response);
    }
}
