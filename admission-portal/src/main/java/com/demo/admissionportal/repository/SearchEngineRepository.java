package com.demo.admissionportal.repository;

import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Search engine repository.
 */
@Repository
public interface SearchEngineRepository extends JpaRepository<Post, Integer> {
    /**
     * Search post page.
     *
     * @param title      the title
     * @param tag        the tag
     * @param schoolName the school name
     * @param code       the code
     * @param pageable   the pageable
     * @return the page
     */
    Page<PostSearchDTO> searchPost(String title, String tag, String schoolName, String code, Pageable pageable);
}
