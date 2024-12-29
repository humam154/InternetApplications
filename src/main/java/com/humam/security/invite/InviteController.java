package com.humam.security.invite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humam.security.utils.GenericResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InviteController {
    
    private final InviteService inviteService;

    @PostMapping("/invite")
    public ResponseEntity<GenericResponse<InviteResponse>> invite(
        @RequestHeader("Authorization") String token,
        @RequestBody InviteRequest request
        ) {
            
            InviteResponse response = inviteService.invite(token, request.getGid(), request.getUid());
        
        return ResponseEntity.ok(GenericResponse.success(response, "Invitation sent!"));
    }
    
}
