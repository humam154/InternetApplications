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
public class GroupResponse {
    
    @JsonProperty("gid")
    private Integer gid;
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("is_owner")
    private boolean isOwner;

    @JsonProperty("description")
    private String description;
    
    @JsonProperty("owner")
    private String owner;

    @JsonProperty("creation_date")
    private Instant creation_date;

    @JsonProperty("members_count")
    private Integer numMembers;
}
