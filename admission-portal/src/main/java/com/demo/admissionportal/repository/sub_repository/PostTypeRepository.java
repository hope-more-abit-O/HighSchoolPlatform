package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.PostType;
import com.demo.admissionportal.entity.sub_entity.id.PostTypeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Post type repository.
 */
@Repository
public interface PostTypeRepository extends JpaRepository<PostType, PostTypeId> {
    /**
     * Find post type by id list.
     *
     * @param id the id
     * @return the list
     */
    List<PostType> findPostTypeByPostId(Integer id);
}
