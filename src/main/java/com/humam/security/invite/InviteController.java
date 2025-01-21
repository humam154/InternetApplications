package com.humam.security.invite;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.humam.security.utils.GenericResponse;

import lombok.RequiredArgsConstructor;


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

    @PostMapping("/accept/{id}")
    public ResponseEntity<GenericResponse<Object>> accept(
        @RequestHeader("Authorization") String token,
        @PathVariable @NotNull @Min(1) Integer id
    )
    {
        try {
            var isAccepted = (Object) inviteService.acceptInvite(token, id);
            return ResponseEntity.ok(GenericResponse.success(isAccepted, "Accepted"));
        }
        catch (IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.error(exception.getMessage()));
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponse.error(exception.getMessage()));
        }
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<GenericResponse<Object>> reject(
        @RequestHeader("Authorization") String token,
        @PathVariable @NotNull @Min(1) Integer id
    )
    {
        try {
            var isRejected = (Object) inviteService.rejectInvite(token, id);
            return ResponseEntity.ok(GenericResponse.success(isRejected, "Rejected"));
        }
        catch (IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.error(exception.getMessage()));
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponse.error(exception.getMessage()));
        }
    }

    @PostMapping("/revoke/{id}")
    public ResponseEntity<GenericResponse<Object>> revoke(
        @RequestHeader("Authorization") String token,
        @PathVariable @NotNull @Min(1) Integer id
    )
    {
        try {
            var isRevoked = (Object) inviteService.revokeInvite(token ,id);
            return ResponseEntity.ok(GenericResponse.success(isRevoked, "Revoked"));
        }
        catch (IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.error(exception.getMessage()));
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponse.error(exception.getMessage()));
        }
    }

    @GetMapping("/inbox")
    public ResponseEntity<GenericResponse<List<InviteResponse>>> inbox(
        @RequestHeader("Authorization") String token
    )
    {
        List<InviteResponse> invites = inviteService.inbox(token);

        if(invites.isEmpty()) {
            return ResponseEntity.ok(GenericResponse.empty("Wow, such empty! You do not have pending invitations."));   
        }
        return ResponseEntity.ok(GenericResponse.success(invites, "Pending invitations retrieved successfully"));
    }

    @GetMapping("/outbox")
    public ResponseEntity<GenericResponse<List<InviteResponse>>> outbox(
        @RequestHeader("Authorization") String token
    )
    {
        List<InviteResponse> invites = inviteService.outbox(token);

        if(invites.isEmpty()) {
            return ResponseEntity.ok(GenericResponse.empty("Wow, such empty! You do not have pending invitations."));   
        }
        return ResponseEntity.ok(GenericResponse.success(invites, "Pending invitations retrieved successfully"));
    }
}
