package com.humam.security.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("action")
    private String action;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("time")
    private LocalDateTime time;
}
