package com.humam.security.group;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.humam.security.utils.GenericResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupSerivce groupSerivce;

    @PostMapping("/create")
    public ResponseEntity<GenericResponse<CreateGroupResponse>> createGroup(
        @RequestHeader("Authorization") String token,
        @RequestBody @Valid CreateGroupRequest request
    ) {
        CreateGroupResponse response = groupSerivce.createGroup(token, request.getName(), request.getDescription());
        return ResponseEntity.ok(GenericResponse.success(response, "Group Created Successfully!"));
    }

}
