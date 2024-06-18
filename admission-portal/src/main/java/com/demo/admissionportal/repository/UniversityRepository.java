package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface University repository.
 */
@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {

    /**
     * Find by username or name optional.
     *
     * @param username the username
     * @param name     the name
     * @return the optional
     */
    Optional<University> findByUsernameOrName(String username, String name);

    /**
     * Find by username or email or code list.
     *
     * @param username the username
     * @param email    the email
     * @param code     the code
     * @return the list
     */
    List<University> findByUsernameOrEmailOrCode(String username, String email, String code);

    /**
     * Find by username or email list.
     *
     * @param username the username
     * @param email    the email
     * @return the list
     */
    List<University> findByUsernameOrEmail(String username, String email);

    /**
     * Find by email university.
     *
     * @param email the email
     * @return the university
     */
    University findByEmail(String email);

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<University> findByUsername(String username);

    /**
     * Find by phone optional.
     *
     * @param phone the phone
     * @return the optional
     */
    Optional<University> findByPhone(String phone);
}
