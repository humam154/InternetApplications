package com.humam.security.group;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humam.security.utils.GenericResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupSerivce groupSerivce;

    @GetMapping("/isowner/{gid}")
    public ResponseEntity<Boolean> isGroupOwne(
        @RequestHeader("Authorization") String token,
        @PathVariable @NotNull @Min(1) Integer gid
        ) {

        return ResponseEntity.ok(groupSerivce.isGroupOwner(token, gid));
    }
    
    @PostMapping("/create")
    public ResponseEntity<GenericResponse<CreateGroupResponse>> createGroup(
        @RequestHeader("Authorization") String token,
        @RequestBody @Valid CreateGroupRequest request
    ) {
        CreateGroupResponse response = groupSerivce.createGroup(token, request.getName(), request.getDescription());
        return ResponseEntity.ok(GenericResponse.success(response, "Group Created Successfully!"));
    }

}
