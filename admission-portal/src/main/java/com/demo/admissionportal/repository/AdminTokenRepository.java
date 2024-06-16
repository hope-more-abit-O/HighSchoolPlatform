package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.AdminToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Admin token repository.
 */
@Repository
public interface AdminTokenRepository extends JpaRepository<AdminToken, Integer> {
    /**
     * Find all valid token by admin list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query("""
            SELECT t 
            FROM AdminToken t INNER JOIN Admin u ON t.admin.id = u.id
            WHERE u.id =  :userId and (t.expired = false or t.revoked = false)
            """)
    List<AdminToken> findAllValidTokenByAdmin(Integer userId);

    /**
     * Find by admin token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<AdminToken> findByAdminToken(String token);
}
