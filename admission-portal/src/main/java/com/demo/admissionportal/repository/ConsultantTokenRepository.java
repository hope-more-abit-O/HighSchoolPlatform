package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.ConsultantToken;
import com.demo.admissionportal.entity.StaffToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Consultant token repository.
 */
@Repository
public interface ConsultantTokenRepository extends JpaRepository<ConsultantToken, Integer> {
    /**
     * Find all valid token by consultant list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query("""
            SELECT t 
            FROM ConsultantToken t INNER JOIN Consultant u ON t.consultant.id = u.id
            WHERE u.id =  :userId and (t.expired = false or t.revoked = false)
            """)
    List<ConsultantToken> findAllValidTokenByConsultant(Integer userId);

    /**
     * Find by consultant token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<ConsultantToken> findByConsultantToken(String token);
}
