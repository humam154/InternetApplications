package com.humam.security.groupmember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humam.security.group.Group;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Integer> {
    List<GroupMember> findByGroupId(Integer groupId);
    List<GroupMember> findByUserId(Integer userId);

    @Query("SELECT COUNT(gm) FROM GroupMember gm WHERE gm.group.id = :groupId")
    int numOfMembers(@Param("groupId") Integer groupId);    

    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.user.id = :userId")
    List<Group> findAllGroupsByUserId(@Param("userId") Integer userId);
}
