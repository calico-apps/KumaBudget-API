package com.calicoapps.kumabudget.security.repository;

import com.calicoapps.kumabudget.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, String> {

    @Query(value = """
            select t from Token t inner join Credentials cr\s
            on t.credentials.email = cr.email\s
            where cr.email = :userEmail and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(String userEmail);

}