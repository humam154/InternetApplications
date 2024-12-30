package com.humam.security.group;


import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupResponse {
    
    @JsonProperty("gid")
    private Integer gid;
    
    @JsonProperty("name")
    private String name;

    private boolean isOwner;

    @JsonProperty("is_owner")
    public boolean getIsOwner() {
     return this.isOwner;
    }

    @JsonProperty("description")
    private String description;
    
    @JsonProperty("owner")
    private String owner;

    @JsonProperty("creation_date")
    private Instant creation_date;

    @JsonProperty("members_count")
    private Integer numMembers;
}
