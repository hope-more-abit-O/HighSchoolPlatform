package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Tag repository.
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {
    /**
     * Find tag by id tag.
     *
     * @param id the id
     * @return the tag
     */
    Tag findTagById(Integer id);

    /**
     * Find by name tag.
     *
     * @param name the name
     * @return the tag
     */
    Tag findTagByname(String name);
}
