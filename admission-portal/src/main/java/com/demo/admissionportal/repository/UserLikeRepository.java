package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface User like repository.
 */
@Repository
public interface UserLikeRepository extends JpaRepository<UserLike, Integer> {
    /**
     * Find by user id and post id user like.
     *
     * @param userId the user id
     * @param postId the post id
     * @return the user like
     */
    @Query(value = "SELECT * " +
            "FROM [user_like] ul " +
            "WHERE ul.user_id = :userId AND ul.post_id = :postId", nativeQuery = true)
    UserLike findByUserIdAndPostId(Integer userId, Integer postId);

    /**
     * Find by user id and university id user like.
     *
     * @param userId       the user id
     * @param universityId the university id
     * @return the user like
     */
    @Query(value = "SELECT ul.* " +
            "FROM [user_like] ul " +
            "JOIN [post] p ON ul.post_id = p.id " +
            "JOIN [consultant_info] ci ON p.create_by = ci.consultant_id " +
            "JOIN [user] u ON u.id = ci.university_id " +
            "WHERE ul.user_id = :userId AND u.id = :universityId ", nativeQuery = true)
    List<UserLike> findByUserIdAndUniversityId(Integer userId, Integer universityId);

    /**
     * Total like post optional.
     *
     * @return the optional
     */
    @Query(value = "SELECT COUNT(ul.post_id) " +
            "FROM university_info ui " +
            "JOIN consultant_info ci ON ci.university_id = ui.university_id " +
            "JOIN post p ON p.create_by = ci.consultant_id OR p.create_by = ui.university_id " +
            "JOIN user_like ul ON p.id = ul.post_id " +
            "WHERE ui.university_id = :universityId", nativeQuery = true)
    Optional<Integer> totalLikePost(Integer universityId);
}
