package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The interface Question repository.
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    /**
     * Find by content question.
     *
     * @param content the content
     * @return the question
     */
    @Query(value = "SELECT * " +
            "FROM question q " +
            "WHERE q.content = N':content'", nativeQuery = true)
    Question findByContent(String content);

    /**
     * Find list status page.
     *
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = "SELECT * " +
            "FROM question q " +
            "WHERE q.status = N'ACTIVE'", nativeQuery = true)
    Page<Question> findListQuestion(Pageable pageable);

    /**
     * Find list question random list.
     *
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM question q " +
            "WHERE q.status = N'ACTIVE' AND id = :questionId", nativeQuery = true)
    Question findQuestionWithStatus(Integer questionId);
}
