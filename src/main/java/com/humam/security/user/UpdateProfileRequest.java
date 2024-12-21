package com.humam.security.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UpdateProfileRequest {

    private String first_name;

    private String last_name;

    private String email;
}
