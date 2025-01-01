package com.humam.security.invite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InviteRepository extends JpaRepository<Invite, Integer> {
    List<Invite> findByGroupId(Integer groupId);
    List<Invite> findByInviteToId(Integer inviteToId);

    @Query("SELECT i FROM Invite i WHERE i.inviteTo.id = :userId AND i.status = :status")
    List<Invite> findByInvitedToAndStatus(@Param("userId") Integer userId, @Param("status") InviteStatus status);


    @Query("SELECT i FROM Invite i WHERE i.invitedBy.id = :userId AND i.status = :status")
    List<Invite> findByInvitedByAndStatus(@Param("userId") Integer userId, @Param("status") InviteStatus status);
}
