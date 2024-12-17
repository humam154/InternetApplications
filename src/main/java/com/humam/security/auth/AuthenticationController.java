
package com.humam.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humam.security.utils.ResponseUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Object> register(
        @Valid @RequestBody RegisterRequest request
    )
    {
        AuthenticationResponse response = service.register(request);
        return ResponseUtil.success("Registration successful", response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> auth (
        @Valid @RequestBody AuthenticateRequest request
    )
    {
        AuthenticationResponse response = service.auth(request);
        return ResponseUtil.success("Authentication successful", response);
    }
}
