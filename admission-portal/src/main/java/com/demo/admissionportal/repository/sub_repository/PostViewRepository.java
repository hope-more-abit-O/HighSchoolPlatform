package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.PostView;
import com.demo.admissionportal.entity.sub_entity.id.PostViewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Post view repository.
 */
@Repository
public interface PostViewRepository extends JpaRepository<PostView, PostViewId> {
    /**
     * Find post view by id post view.
     *
     * @param postViewId the post view id
     * @return the post view
     */
    PostView findByPostId(Integer postViewId);
}
