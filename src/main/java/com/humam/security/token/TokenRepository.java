package com.humam.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(
            "SELECT t FROM Token t inner join User u on t.user.id = u.id " +
                    "WHERE u.id = :userId AND (t.expired = false or t.revoked = false)"
    )
    List<Token> findAllValidTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
