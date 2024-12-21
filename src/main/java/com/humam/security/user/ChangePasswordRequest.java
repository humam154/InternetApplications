package com.humam.security.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Builder
@Getter
@Setter
public class ChangePasswordRequest {

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;
}
