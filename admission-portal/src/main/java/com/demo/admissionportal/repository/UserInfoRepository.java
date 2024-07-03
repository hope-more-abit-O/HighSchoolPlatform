package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM user_info uf " +
            "JOIN [user] u ON u.id = uf.user_id " +
            "WHERE (:username IS NULL OR u.username LIKE %:username%) " +
            "AND (:email IS NULL OR u.email LIKE %:email%)",
            nativeQuery = true)
    List<UserInfo> findAllUser(String username, String email);
}
