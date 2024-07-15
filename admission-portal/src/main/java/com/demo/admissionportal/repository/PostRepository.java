package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Post repository.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    /**
     * Find post by id post.
     *
     * @param id the id
     * @return the post
     */
    Post findFirstById(Integer id);


    @Query(value = "SELECT p.* FROM post p " +
            "INNER JOIN post_tag pt ON p.id = pt.post_id " +
            "INNER JOIN tag t ON pt.tag_id = t.id " +
            "INNER JOIN [user] u ON p.create_by = u.id " +
            "WHERE t.name LIKE %:tagName%", nativeQuery = true)
    List<Post> findPostsByTagName(@Param("tagName") String tagName);

    @Query(value = "SELECT p.* FROM post p " +
            "INNER JOIN post_tag pt ON p.id = pt.post_id " +
            "INNER JOIN tag t ON pt.tag_id = t.id " +
            "INNER JOIN [user] u ON p.create_by = u.id " +
            "ORDER BY p.create_time DESC", nativeQuery = true)
    List<Post> findPostByDescCreateTime();
}
