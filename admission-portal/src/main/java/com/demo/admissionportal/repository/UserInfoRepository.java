package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface User info repository.
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
}
