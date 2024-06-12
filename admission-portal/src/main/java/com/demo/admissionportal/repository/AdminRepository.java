package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Admin repository.
 */
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
}