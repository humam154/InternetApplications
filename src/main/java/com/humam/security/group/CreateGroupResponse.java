package com.humam.security.group;


import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupResponse {
    
    @JsonProperty("gid")
    private Integer gid;
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
    
    @JsonProperty("owner")
    private String owner;

    @JsonProperty("creation_time")
    private Instant creation_time;
}
