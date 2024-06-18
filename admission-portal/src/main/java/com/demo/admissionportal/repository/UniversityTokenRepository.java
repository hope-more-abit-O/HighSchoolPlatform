package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.StaffToken;
import com.demo.admissionportal.entity.UniversityToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface University token repository.
 */
@Repository
public interface UniversityTokenRepository extends JpaRepository<UniversityToken, Integer> {
    /**
     * Find all valid token by university list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query("""
            SELECT t 
            FROM UniversityToken t INNER JOIN University u ON t.university.id = u.id
            WHERE u.id =  :userId and (t.expired = false or t.revoked = false)
            """)
    List<UniversityToken> findAllValidTokenByUniversity(Integer userId);

    /**
     * Find by university token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<UniversityToken> findByUniversityToken(String token);
}
