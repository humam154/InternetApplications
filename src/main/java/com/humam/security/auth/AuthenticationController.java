
package com.humam.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    )
    {
        if(request.getPassword().length() > 8) {
            return ResponseEntity.ok(service.register(request));
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> auth (
            @RequestBody AuthenticateRequest request
    )
    {
        return ResponseEntity.ok(service.auth(request));
    }
}
