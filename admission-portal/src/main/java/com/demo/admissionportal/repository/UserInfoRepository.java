package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
