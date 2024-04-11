package com.calicoapps.kumabudget.auth.data.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            select t from Token t inner join AuthUser u\s
            on t.authUser.email = u.email\s
            where u.email = :userEmail and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(String userEmail);

    Optional<Token> findByToken(String token);

}