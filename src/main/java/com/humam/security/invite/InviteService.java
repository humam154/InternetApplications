package com.humam.security.invite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humam.security.group.Group;
import com.humam.security.group.GroupRepository;
import com.humam.security.group.GroupService;
import com.humam.security.groupmember.GroupMember;
import com.humam.security.groupmember.GroupMemberRepository;
import com.humam.security.token.TokenRepository;
import com.humam.security.user.User;
import com.humam.security.user.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final InviteRepository inviteRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public InviteResponse invite(String token, Integer gid, Integer uid) {
        if (!groupService.isGroupOwner(token, gid)) {
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
            .status(InviteStatus.PENDING)
            .inviteDate(Instant.now())
            .build());

        return InviteResponse.builder()
            .group_name(group.getName())
            .inviter(inviter.getFirst_name() + " " + inviter.getLast_name())
            .invitee(invitee.getFirst_name() + " " + invitee.getLast_name())
            .build();
    }

    @Transactional
    public boolean acceptInvite(String token, Integer id) throws Exception {
        var optionalInvite = inviteRepository.findById(id);

        if(optionalInvite.isPresent()) {
            var invite = optionalInvite.get();

            User invitee = tokenRepository.findByToken(token.replaceFirst("^Bearer ", ""))
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"))
            .getUser();

            if(!invitee.equals(invite.getInviteTo())) {
                throw new IllegalAccessException("You are not the invitee");
            }

            invite.setStatus(InviteStatus.ACCEPTED);

            inviteRepository.save(invite);
            
            groupMemberRepository.save(GroupMember.builder()
                .group(invite.getGroup())
                .user(invite.getInviteTo())
                .joinDate(Instant.now())
                .build());

            return true;
        } else {
            throw new IllegalArgumentException("Invalid invite ID");
        }
    }

    public boolean rejectInvite(Integer id) {
        var optionalInvite = inviteRepository.findById(id);

        if(optionalInvite.isPresent()) {
            var invite = optionalInvite.get();

            invite.setStatus(InviteStatus.REJECTED);

            inviteRepository.save(invite);

            return true;
        } else {
            throw new IllegalArgumentException("Invalid invite ID");
        }
    }

    public List<InviteResponse> inbox(String token) {
        token = token.replaceFirst("^Bearer ", "");
        User user = tokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"))
            .getUser();

        List<Invite> invites = inviteRepository.findByInvitedToAndStatus(user.getId(), InviteStatus.PENDING);

        return invites.stream().map(invite -> InviteResponse.builder()
            .id(invite.getId())
            .group_name(invite.getGroup().getName())
            .inviter(invite.getInvitedBy().getFirst_name() + " " + invite.getInvitedBy().getLast_name())
            .invitee(invite.getInviteTo().getFirst_name() + " " + invite.getInviteTo().getLast_name())
            .inviteDate(invite.getInviteDate())
            .build()
        ).toList();
    }


    public List<InviteResponse> outbox(String token) {
        token = token.replaceFirst("^Bearer ", "");
        User user = tokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"))
            .getUser();

        List<Invite> invites = inviteRepository.findByInvitedByAndStatus(user.getId(), InviteStatus.PENDING);

        return invites.stream().map(invite -> InviteResponse.builder()
            .id(invite.getId())
            .group_name(invite.getGroup().getName())
            .inviter(invite.getInvitedBy().getFirst_name() + " " + invite.getInvitedBy().getLast_name())
            .invitee(invite.getInviteTo().getFirst_name() + " " + invite.getInviteTo().getLast_name())
            .inviteDate(invite.getInviteDate())
            .build()
        ).toList();
    }
}