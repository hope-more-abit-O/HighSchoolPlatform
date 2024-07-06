package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.AdminInfo;
import com.demo.admissionportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Admin info repository.
 */
public interface AdminInfoRepository extends JpaRepository<AdminInfo, Integer> {
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

    /**
     * Find admin info by id admin info.
     *
     * @param id the id
     * @return the admin info
     */
    AdminInfo findAdminInfoById(Integer id);
}
