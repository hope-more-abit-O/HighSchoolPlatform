package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface User like repository.
 */
@Repository
public interface UserLikeRepository extends JpaRepository<UserLike, Integer> {
}
