package com.humam.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND u.id NOT IN (" +
            "SELECT u.id FROM User u " +
            "JOIN GroupMember gu ON gu.user.id = u.id " +
            "JOIN Group g ON g.id = gu.group.id)")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);


    @Query("SELECT u FROM User u WHERE u.activationCode = :code")
    Optional<User> findByCode(@Param("code") String code);

}
