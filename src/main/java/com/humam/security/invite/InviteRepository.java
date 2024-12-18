package com.humam.security.invite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Integer> {
    List<Invite> findByGroupId(Integer groupId);
    List<Invite> findByInviteToId(Integer inviteToId);
    List<Invite> findByAccepted(Boolean accepted);
}
