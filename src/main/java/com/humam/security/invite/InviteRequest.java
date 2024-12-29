package com.humam.security.invite;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteRequest {

    @NotNull(message = "Group id is required")
    private Integer gid;

    @NotNull(message = "User id is required")
    private Integer uid;
}
