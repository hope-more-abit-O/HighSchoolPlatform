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

    @Query(value = "SELECT * FROM Staff s WHERE " +
            "(:username IS NULL OR s.username LIKE %:username%) AND " +
            "(:name IS NULL OR s.name LIKE %:name%) AND " +
            "(:email IS NULL OR s.email LIKE %:email%) AND " +
            "(:phone IS NULL OR s.phone LIKE %:phone%)", nativeQuery = true)
    Page<StaffInfo> findAll(String username, String name, String email, String phone, Pageable pageable);
}
