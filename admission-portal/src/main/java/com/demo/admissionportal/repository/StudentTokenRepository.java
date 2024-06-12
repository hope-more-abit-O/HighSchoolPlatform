package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.StudentToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * The interface Student token repository.
 */
public interface StudentTokenRepository extends JpaRepository<StudentToken, Integer> {
    /**
     * Find all valid token by user list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query("""
            SELECT t 
            FROM StudentToken t INNER JOIN Student u ON t.student.id = u.id
            WHERE u.id =  :userId and (t.expired = false or t.revoked = false)
            """)
    List<StudentToken> findAllValidTokenByUser(Integer userId);

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<StudentToken> findByTokenStudent(String token);
}
