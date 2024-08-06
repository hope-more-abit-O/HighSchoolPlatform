package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The interface User like repository.
 */
@Repository
public interface UserLikeRepository extends JpaRepository<UserLike, Integer> {
    @Query(value = "SELECT * " +
            "FROM [user_like] ul " +
            "WHERE ul.user_id = :userId AND ul.post_id = :postId", nativeQuery = true)
    UserLike findByUserIdAndPostId(Integer userId, Integer postId);
}
