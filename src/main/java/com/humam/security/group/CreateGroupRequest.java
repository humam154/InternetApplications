package com.humam.security.group;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupRequest {

    @NotEmpty(message = "Group name must not be empty")
    private String name;

    @NotEmpty(message = "Group description must not be empty")
    private String description;
}
