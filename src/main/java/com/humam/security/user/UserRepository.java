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

    @Query("SELECT u FROM User u LEFT JOIN GroupMember gm ON u.id = gm.user.id AND gm.group.id = :groupId " +
    "WHERE (LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
    "gm.id IS NULL")
    List<User> searchUsersNotInGroup(@Param("searchTerm") String searchTerm, @Param("groupId") Integer groupId);

    @Query("SELECT u FROM User u LEFT JOIN GroupMember gm ON u.id = gm.user.id AND gm.group.id = :groupId " +
    "WHERE (LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
    "gm.id IS NOT NULL")
    List<User> searchUsersInGroup(@Param("searchTerm") String searchTerm, @Param("groupId") Integer groupId);

    @Query("SELECT u FROM User u WHERE u.activationCode = :code")
    Optional<User> findByCode(@Param("code") String code);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    Optional<User> findAdmin(Role role);

    @Query("SELECT u FROM User u WHERE " +
    "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);

}
