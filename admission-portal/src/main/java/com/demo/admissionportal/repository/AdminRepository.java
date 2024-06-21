package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Admin repository.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    /**
     * Find admin by username and email admin.
     *
     * @param email    the email
     * @param username the username
     * @return the admin
     */
    Admin findAdminByUsernameAndEmail(String email, String username);

    /**
     * Find admin by phone admin.
     *
     * @param phone the phone
     * @return the admin
     */
    Admin findAdminByPhone(String phone);

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<Admin> findByUsername(String username);

    /**
     * Find by email admin.
     *
     * @param email the email
     * @return the admin
     */
    Admin findByEmail(String email);

    Optional<Admin> findFirstByUsernameOrEmail(String username, String email);

    Optional<Admin> findFirstByPhoneAndIdNot(String phone, Integer id);
    Optional<Admin> findFirstByPhone(String phone);

    Optional<Admin> findFirstByEmail(String email);

    Optional<Admin> findFirstByEmailAndIdNot(String email, Integer id);

    Optional<Admin> findFirstByUsername(String username);

    Optional<Admin> findFirstByUsernameAndIdNot(String username, Integer id);
}