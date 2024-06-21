package com.demo.admissionportal.repository;

import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.StaffResponseDTO;
import com.demo.admissionportal.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query(value = "SELECT * FROM Staff s WHERE " +
            "(:username IS NULL OR s.username LIKE %:username%) AND " +
            "(:name IS NULL OR s.name LIKE %:name%) AND " +
            "(:email IS NULL OR s.email LIKE %:email%) AND " +
            "(:phone IS NULL OR s.phone LIKE %:phone%)", nativeQuery = true)
    Page<Staff> findAll(String username, String name, String email, String phone, Pageable pageable);

    Optional<Staff> findFirstByUsernameOrEmail(String username, String email);



}

