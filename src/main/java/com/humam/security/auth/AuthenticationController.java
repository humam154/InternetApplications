
package com.humam.security.auth;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.humam.security.utils.GenericResponse;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<AuthenticationResponse>> register(
        @Valid @RequestBody RegisterRequest request
    ) throws MessagingException {
        AuthenticationResponse response = service.register(request);
        return ResponseEntity.ok(GenericResponse.success(response, "check your email for code"));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> auth (
        @Valid @RequestBody AuthenticateRequest request
    )
    {
        AuthenticationResponse response = service.auth(request);
        return ResponseEntity.ok(GenericResponse.success(response, "Authentication successful"));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<Object> confirm(
            @RequestParam String code
    ) {
        AuthenticationResponse response = service.activateAccount(code);

        return ResponseEntity.ok(GenericResponse.success(response, "account activated successfully"));
    }

    @GetMapping("/request-reset")
    public ResponseEntity<Object> requestReset(
            @RequestParam String email
    ) throws MessagingException {
        RequestResetPasswordResponse response = service.requestPasswordReset(email);
        return ResponseEntity.ok(GenericResponse.success(response, "message sent"));
    }

    @PostMapping("/forgot")
    public ResponseEntity<Object> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {
        var response = service.changePassword(request);
        return ResponseEntity.ok(GenericResponse.success(response, "password changed successfully"));
    }
}
