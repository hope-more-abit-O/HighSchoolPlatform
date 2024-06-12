package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Staff repository.
 */
@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    /**
     * Find by name staff.
     *
     * @param phone the phone
     * @return the staff
     */
    Staff findByPhone(String phone);

    /**
     * Find by email or username staff.
     *
     * @param email    the email
     * @param username the username
     * @return the staff
     */
    Staff findByEmailOrUsername(String email, String username);
}
