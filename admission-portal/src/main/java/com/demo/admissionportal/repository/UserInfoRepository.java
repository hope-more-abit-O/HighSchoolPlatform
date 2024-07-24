package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UserInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.Optional;

/**
 * The interface User info repository.
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    /**
     * Find user info by id user info.
     *
     * @param id the id
     * @return the user info
     */
    UserInfo findUserInfoById(Integer id);

    /**
     * Find users with user info list.
     *
     * @param username the username
     * @param email    the email
     * @param pageable the pageable
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM [user_info] uf " +
            "JOIN [user] u ON u.id = uf.user_id " +
            "WHERE (:username IS NULL OR u.username LIKE %:username%) " +
            "AND (:firstName IS NULL OR uf.first_name LIKE %:firstName%) " +
            "AND (:middleName IS NULL OR uf.middle_name LIKE %:middleName%) " +
            "AND (:lastName IS NULL OR uf.last_name LIKE %:lastName%) " +
            "AND (:phone IS NULL OR uf.phone LIKE %:phone%) " +
            "AND (:email IS NULL OR u.email LIKE %:email%) " +
            "AND (:specificAddress IS NULL OR uf.specific_address LIKE %:specificAddress%) " +
            "AND (:educationLevel IS NULL OR uf.education_level LIKE %:educationLevel%) " +
            "AND (:status IS NULL OR u.status LIKE %:status%) ", nativeQuery = true)
    Page<UserInfo> findAll(
            @Param("username") String username,
            @Param("firstName") String firstName,
            @Param("middleName") String middleName,
            @Param("lastName") String lastName,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("specificAddress") String specificAddress,
            @Param("educationLevel") String educationLevel,
            @Param("status") String status,
            Pageable pageable);


    /**
     * Find first by phone optional.
     *
     * @param phone the phone
     * @return the optional
     */
    Optional<UserInfo> findFirstByPhone(String phone);
}