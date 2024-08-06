package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
