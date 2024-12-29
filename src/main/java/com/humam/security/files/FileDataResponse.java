package com.humam.security.files;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDataResponse {

    private Integer id;

    private String name;
    
    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("created_by")
    private String createdByUser;

    private Boolean accepted;

    @JsonProperty("in_use")
    private Boolean inUse;
    
    private Integer version;

}
