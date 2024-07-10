package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
