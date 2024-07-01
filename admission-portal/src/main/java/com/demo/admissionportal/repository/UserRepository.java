package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Find by email optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findByEmail(String username);
}
