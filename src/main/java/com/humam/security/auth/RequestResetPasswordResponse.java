package com.humam.security.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestResetPasswordResponse {

    @JsonProperty("email")
    private String email;

    @JsonProperty("text")
    private String text;
}