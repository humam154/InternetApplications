package com.humam.security.groupmember;

import java.time.Instant;

import com.humam.security.group.Group;
import com.humam.security.user.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`group_members`")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    
    @Column(name = "join_date",updatable = false)
    private Instant joinDate;
}
