package com.demo.admissionportal.repository;

import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * The interface Search engine repository.
 */
@Repository
public interface SearchEngineRepository extends JpaRepository<Post, Integer> {
    /**
     * Search post page.
     *
     * @param content the content
     * @return the page
     */
    List<PostSearchDTO> searchPost(String content);

    /**
     * Search post by filter list.
     *
     * @param typeId     the type id
     * @param locationId the location id
     * @param startDate  the start date
     * @param endDate    the end date
     * @param authorId   the author id
     * @return the list
     */
    List<PostSearchDTO> searchPostByFilter(Integer typeId, Integer locationId, LocalDate startDate, LocalDate endDate, Integer authorId);
}
