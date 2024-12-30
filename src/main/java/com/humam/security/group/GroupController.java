package com.humam.security.group;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.humam.security.utils.GenericResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;


@CrossOrigin(origins = "http://localhost:5173")
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

    @DeleteMapping("/{gid}")
    public ResponseEntity<GenericResponse<Object>> delete(
            @PathVariable @NotNull @Min(1) Integer gid
    )
    {
        try {
            var isDeleted = (Object) groupService.delete(gid);
            return ResponseEntity.ok(GenericResponse.success(isDeleted, "Group deleted successfully"));
        }
        catch (IllegalStateException illegalStateException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponse.error(illegalStateException.getMessage()));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.error(illegalArgumentException.getMessage()));
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponse.error(exception.getMessage()));
        }

    }
}
