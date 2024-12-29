package com.humam.security.invite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteResponse {

    @JsonProperty("group_name")
    private String group_name;
    
    @JsonProperty("inviter")
    private String inviter;

    @JsonProperty("invitee")
    private String invitee;
    

}
