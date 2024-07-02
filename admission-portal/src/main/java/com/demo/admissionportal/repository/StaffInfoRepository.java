package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StaffInfoRepository extends JpaRepository<StaffInfo, Integer> {

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

    @Query(value = "SELECT * FROM staff_info s JOIN [user] u ON s.staff_id = u.id WHERE " +
            "(:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:firstName IS NULL OR s.first_name LIKE %:firstName%) AND " +
            "(:middleName IS NULL OR s.middle_name LIKE %:middleName%) AND " +
            "(:lastName IS NULL OR s.last_name LIKE %:lastName%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:phone IS NULL OR s.phone LIKE %:phone%) " +
            "(:status IS NULL OR s.status LIKE %:status%)" +
            "ORDER BY s.create_time DESC", nativeQuery = true)
    Page<StaffInfo> findAll(String username, String firstName, String lastName, String email, String phone, Pageable pageable);

    Optional<User> findByStatus(String status);
}
