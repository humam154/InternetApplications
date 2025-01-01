package com.humam.security.invite;

import com.humam.security.group.Group;
import com.humam.security.user.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`invites`")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "invited_by", nullable = false)
    private User invitedBy;

    @ManyToOne
    @JoinColumn(name = "invite_to", nullable = false)
    private User inviteTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteStatus status; 
}
