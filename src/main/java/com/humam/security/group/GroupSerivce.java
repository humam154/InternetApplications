package com.humam.security.group;

import com.humam.security.user.User;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humam.security.groupmember.GroupMember;
import com.humam.security.groupmember.GroupMemberRepository;
import com.humam.security.token.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupSerivce {

    private final TokenRepository tokenRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public boolean isGroupOwner(String token, Integer gid) {
        token = token.replaceFirst("^Bearer ", "");
        Integer userId = tokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"))
            .getUser()
            .getId();
    
        return groupRepository.isGroupOwner(userId, gid);
    }
    
    @Transactional
    public CreateGroupResponse createGroup(String token, String name, String description) {
        token = token.replaceFirst("^Bearer ", "");
        User user = tokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"))
            .getUser();

        Group group = groupRepository.save(Group.builder()
        .createdBy(user)
        .name(name)
        .description(description)
        .creationDate(Instant.now())
        .build());

        groupMemberRepository.save(GroupMember.builder()
        .group(group)
        .user(user)
        .joinDate(Instant.now())
        .build());

        return CreateGroupResponse.builder()
        .gid(group.getId())
        .name(name)
        .description(description)
        .owner(user.getFirst_name() + " " + user.getLast_name())
        .creation_time(group.getCreationDate())
        .build();
    }
}
