
package com.humam.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    )
    {
        AuthenticationResponse response = service.register(request);
        return ResponseEntity.ok(GenericResponse.success(response, "Registeration successful"));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> auth (
        @Valid @RequestBody AuthenticateRequest request
    )
    {
        AuthenticationResponse response = service.auth(request);
        return ResponseEntity.ok(GenericResponse.success(response, "Authentication successful"));
    }
}
