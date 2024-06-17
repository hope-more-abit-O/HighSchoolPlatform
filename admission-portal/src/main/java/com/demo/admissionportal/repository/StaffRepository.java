package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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


    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<Staff> findByUsername(String username);


    /**
     * Find by email staff.
     *
     * @param email the email
     * @return the staff
     */
    Staff findByEmail(String email);

    @Query("SELECT s FROM Staff s WHERE (:name IS NULL OR s.name LIKE %:name%) AND (:phone IS NULL OR s.phone LIKE %:phone%)")
    Page<Staff> findAll(@Param("name") String name, @Param("phone") String phone, Pageable pageable);

}
