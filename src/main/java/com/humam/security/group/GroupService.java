package com.humam.security.group;

import com.humam.security.user.User;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humam.security.groupmember.GroupMember;
import com.humam.security.groupmember.GroupMemberRepository;
import com.humam.security.token.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {

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
    public GroupResponse createGroup(String token, String name, String description) {
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

        return GroupResponse.builder()
        .gid(group.getId())
        .name(name)
        .description(description)
        .owner(user.getFirst_name() + " " + user.getLast_name())
        .creation_date(group.getCreationDate())
        .build();
    }

    public boolean delete(Integer gid) {
        Optional<Group> group = groupRepository.findById(gid);

        if(group.isPresent()) {
            int membersCount = groupRepository.numOfMembers(gid);

            if(membersCount > 0) {
                groupRepository.deleteById(gid);
                return true;
            } else {
                throw new IllegalStateException("group has members");
            }
        } else {
            throw new IllegalArgumentException("group not found");
        }
    }

}
