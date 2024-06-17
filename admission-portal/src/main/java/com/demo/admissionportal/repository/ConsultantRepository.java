package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Consultant repository.
 */
@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Integer> {

    /**
     * Find by email or username optional.
     *
     * @param email    the email
     * @param username the username
     * @return the optional
     */
    Optional<Consultant> findByEmailOrUsername(String email, String username);

    /**
     * Find by phone optional.
     *
     * @param phone the phone
     * @return the optional
     */
    Optional<Consultant> findByPhone(String phone);

    /**
     * Find by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<Consultant> findByEmail(String email);

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<Consultant> findByUsername(String username);
}
