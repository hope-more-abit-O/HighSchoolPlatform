package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.StaffToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * The interface Staff token repository.
 */
public interface StaffTokenRepository extends JpaRepository<StaffToken, Integer> {

    /**
     * Find all valid token by staff list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query("""
            SELECT t 
            FROM StaffToken t INNER JOIN Staff u ON t.staff.id = u.id
            WHERE u.id =  :userId and (t.expired = false or t.revoked = false)
            """)
    List<StaffToken> findAllValidTokenByStaff(Integer userId);

    /**
     * Find by token staff optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<StaffToken> findByStaffToken(String token);

}
