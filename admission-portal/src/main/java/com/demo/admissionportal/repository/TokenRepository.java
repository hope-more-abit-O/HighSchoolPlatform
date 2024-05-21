package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Token repository.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    /**
     * Find all valid token by user list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query("""
            SELECT t 
            FROM Token t INNER JOIN User u ON t.user.id = u.id
            WHERE u.id =  :userId and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenByUser(Integer userId);

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<Token> findByToken(String token);
}
