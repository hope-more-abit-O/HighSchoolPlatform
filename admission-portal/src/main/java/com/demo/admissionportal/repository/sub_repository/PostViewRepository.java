package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Post view repository.
 */
@Repository
public interface PostViewRepository extends JpaRepository<PostView, Integer> {
    /**
     * Find post view by id post view.
     *
     * @param id the id
     * @return the post view
     */
    PostView findPostViewById(Integer id);
}
