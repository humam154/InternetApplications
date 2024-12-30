package com.humam.security.invite;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.humam.security.utils.GenericResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;


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

    @GetMapping("/accept")
    public ResponseEntity<GenericResponse<Object>> accept(
        @PathVariable @NotNull @Min(1) Integer id
    )
    {
        try {
            var isAccepted = (Object) inviteService.acceptInvite(id);
            return ResponseEntity.ok(GenericResponse.success(isAccepted, "Accepted"));
        }
        catch (IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.error(exception.getMessage()));
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponse.error(exception.getMessage()));
        }
    }
}
