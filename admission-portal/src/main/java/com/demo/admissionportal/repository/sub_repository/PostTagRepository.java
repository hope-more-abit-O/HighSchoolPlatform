package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.PostTag;
import com.demo.admissionportal.entity.sub_entity.id.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Post tag repository.
 */
@Repository
public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
    /**
     * Find post tag by id post tag.
     *
     * @param id the id
     * @return the post tag
     */
    List<PostTag> findPostTagByPostId(Integer id);
}
