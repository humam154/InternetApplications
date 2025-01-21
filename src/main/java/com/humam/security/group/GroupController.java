package com.humam.security.group;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.humam.security.utils.GenericResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/isowner/{gid}")
    public ResponseEntity<Boolean> isGroupOwner(
        @RequestHeader("Authorization") String token,
        @PathVariable @NotNull @Min(1) Integer gid
        ) {

        return ResponseEntity.ok(groupService.isGroupOwner(token, gid));
    }
    
    @PostMapping("/create")
    public ResponseEntity<GenericResponse<GroupResponse>> createGroup(
        @RequestHeader("Authorization") String token,
        @RequestBody @Valid CreateGroupRequest request
    ) {
        GroupResponse response = groupService.createGroup(token, request.getName(), request.getDescription());
        return ResponseEntity.ok(GenericResponse.success(response, "Group Created Successfully!"));
    }

    @DeleteMapping("/delete/{gid}")
    public ResponseEntity<GenericResponse<Object>> delete(
        // idk why but adding this fixed everything
        @RequestHeader("Authorization") String token,
        @PathVariable @NotNull @Min(1) Integer gid
    )
    {
        boolean isDeleted = groupService.delete(gid);
        return ResponseEntity.ok(GenericResponse.empty("Group deleted successfully"));
    }

    @GetMapping("/")
    public ResponseEntity<GenericResponse<List<GroupResponse>>> groups(
        @RequestHeader("Authorization") String token
    ) 
    {
        List<GroupResponse> groups = groupService.groups(token);
        return ResponseEntity.ok(GenericResponse.success(groups, "Groups retrieved successfully"));
    }

    @DeleteMapping("/member/{gid}/{uid}")
    public ResponseEntity<GenericResponse<Object>> deleteMember(
            @RequestHeader("Authorization") String token,
            @PathVariable @NotNull @Min(1) Integer gid,
            @PathVariable @NotNull @Min(1) Integer uid
    ) {
        try{
            String response = groupService.removeMember(token, gid, uid);
            return ResponseEntity.ok(GenericResponse.success(response, "Member deleted successfully"));
        }
        catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponse.error(e.getMessage()));
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponse.error(exception.getMessage()));
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse<List<GroupResponse>>> getAllGroups(
            @RequestHeader("Authorization") String token
    ) {
        try{
            var response = groupService.getAllGroups(token);
            return ResponseEntity.ok(GenericResponse.success(response, "groups fetched successfully"));
        }
        catch (IllegalStateException | IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponse.error(exception.getMessage()));
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponse.error(exception.getMessage()));
        }
    }
    
}
