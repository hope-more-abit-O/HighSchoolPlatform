package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.Role;
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

    /**
     * Find by role optional.
     *
     * @param role the role
     * @return the optional
     */
    Optional<User> findByRole(Role role);


    /**
     * Find user by id user.
     *
     * @param id the id
     * @return the user
     */
    User findUserById(Integer id);

    /**
     * Find first by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findFirstByUsername(String username);

    /**
     * Find first by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<User> findFirstByEmail(String email);

    /**
     * Find user by email user.
     *
     * @param email the email
     * @return the user
     */
    User findUserByEmail(String email);
//    Optional<User> findByUsernameAndProviderId(String username, String providerId);

}
