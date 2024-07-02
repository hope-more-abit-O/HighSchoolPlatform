package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
