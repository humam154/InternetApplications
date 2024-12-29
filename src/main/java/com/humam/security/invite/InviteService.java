package com.humam.security.invite;

import org.springframework.stereotype.Service;
import com.humam.security.group.Group;
import com.humam.security.group.GroupRepository;
import com.humam.security.group.GroupSerivce;
import com.humam.security.token.TokenRepository;
import com.humam.security.user.User;
import com.humam.security.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final InviteRepository inviteRepository;
    private final GroupRepository groupRepository;
    private final GroupSerivce groupSerivce;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public InviteResponse invite(String token, Integer gid, Integer uid) {
        if (!groupSerivce.isGroupOwner(token, gid)) {
            throw new IllegalArgumentException("Only the group owner can send invites.");
        }

        Group group = groupRepository.findById(gid)
            .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));

        User invitee = userRepository.findById(uid)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        boolean isMember = group.getGroupMembers().stream()
            .anyMatch(member -> member.getUser().getId().equals(uid));

        if (isMember) {
            throw new IllegalArgumentException("The user is already a member of this group.");
        }

        User inviter = tokenRepository.findByToken(token.replaceFirst("^Bearer ", ""))
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"))
            .getUser();

        inviteRepository.save(Invite.builder()
            .group(group)
            .invitedBy(inviter)
            .inviteTo(invitee)
            .accepted(false)
            .build());

        return InviteResponse.builder()
            .group_name(group.getName())
            .inviter(inviter.getFirst_name() + " " + inviter.getLast_name())
            .invitee(invitee.getFirst_name() + " " + invitee.getLast_name())
            .build();
    }
}

