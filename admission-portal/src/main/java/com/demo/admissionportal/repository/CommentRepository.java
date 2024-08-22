package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Comment repository.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Find first by post id list.
     *
     * @param postId the post id
     * @return the list
     */
    List<Comment> findByPostId(Integer postId);

    /**
     * Find by post id and comment parent id list.
     *
     * @param postId          the post id
     * @param commentParentId the comment parent id
     * @return the list
     */
    List<Comment> findByPostIdAndCommentParentId(Integer postId, Integer commentParentId);

    /**
     * Count by post id long.
     *
     * @param postId the post id
     * @return the long
     */
    @Query(value = "SELECT COUNT(*) " +
            "FROM [comment] m " +
            "WHERE m.comment_status = 'ACTIVE' AND m.post_id = :postId ", nativeQuery = true)
    int countByPostId(Integer postId);

    @Query(value = "SELECT COUNT(c.post_id) " +
            "FROM university_info ui " +
            "JOIN consultant_info ci ON ci.university_id = ui.university_id " +
            "JOIN post p ON p.create_by = ci.consultant_id OR p.create_by = ui.university_id " +
            "JOIN comment c ON p.id = c.post_id " +
            "WHERE ui.university_id = :universityId", nativeQuery = true)
    Optional<Integer> totalComment(Integer universityId);
}
