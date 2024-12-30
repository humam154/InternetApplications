package com.humam.security.group;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.humam.security.user.User;
import java.util.List;



@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    
    Optional<Group> findById(Integer id);
    List<Group> findByCreatedBy(User createdBy);    
    
    @Query("SELECT CASE WHEN g.createdBy.id = :userId THEN true ELSE false END FROM Group g WHERE g.id = :groupId")
    boolean isGroupOwner(@Param("userId") Integer userId, @Param("groupId") Integer groupId);
}