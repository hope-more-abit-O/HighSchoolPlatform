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
}
