package com.humam.security.group;

import com.humam.security.user.Role;
import com.humam.security.user.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humam.security.groupmember.GroupMember;
import com.humam.security.groupmember.GroupMemberRepository;
import com.humam.security.token.TokenRepository;

import lombok.RequiredArgsConstructor;

import static com.humam.security.user.Role.*;

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

    @Transactional
    public boolean delete(Integer gid) {
        Optional<Group> group = groupRepository.findById(gid);

        if(group.isPresent()) {
            int membersCount = groupMemberRepository.numOfMembers(gid);

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


    @Transactional
    public List<GroupResponse> groups(String token) {
        token = token.replaceFirst("^Bearer ", "");
        User user = tokenRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"))
            .getUser();
        List<Group> groups = groupMemberRepository.findAllGroupsByUserId(user.getId());

        return groups.stream().map(group -> GroupResponse.builder()
            .gid(group.getId())
            .name(group.getName())
            .description(group.getDescription())
            .isOwner(group.getCreatedBy().getId() == user.getId() ? true : false)
            .owner(group.getCreatedBy().getFirst_name() + " " + group.getCreatedBy().getLast_name())
            .numMembers(groupMemberRepository.numOfMembers(group.getId()))
            .creation_date(group.getCreationDate())
            .build()
        ).toList();
    }

    public String removeMember(String token, Integer gid, Integer memberId){
        boolean owner = this.isGroupOwner(token, gid);
        if(owner){
            GroupMember groupMember = groupMemberRepository.findRecordByGroupIdAndUserId(gid, memberId);
            groupMemberRepository.delete(groupMember);
            return "removed successfully";
        } else {
            throw new IllegalStateException("not group owner");
        }
    }

    public List<GroupResponse> getAllGroups(String token) {
        token = token.replaceFirst("^Bearer ", "");
        User user = tokenRepository.findByToken(token).orElseThrow(
                () -> new IllegalArgumentException("Invalid token")
        ).getUser();

        if(user.getRole() == ADMIN) {
            List<Group> groups = groupRepository.findAll();

            return groups.stream().map( group -> GroupResponse.builder()
                    .gid(group.getId())
                    .name(group.getName())
                    .description(group.getDescription())
                    .isOwner(group.getCreatedBy().getId() == user.getId() ? true : false)
                    .owner(group.getCreatedBy().getFirst_name() + " " + group.getCreatedBy().getLast_name())
                    .creation_date(group.getCreationDate())
                    .numMembers(groupMemberRepository.numOfMembers(group.getId()))
                    .build()

            ).toList();
        } else {
            throw new IllegalStateException("not admin");
        }
    }
}
