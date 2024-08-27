package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
     * @param content  the content
     * @param status   the status
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = "SELECT * FROM question q " +
            "WHERE (:content IS NULL OR q.content LIKE N'%' + :content + '%') " +
            "AND (:status IS NULL OR q.status = :status)",
            nativeQuery = true)
    Page<Question> findListQuestion(@Param("content") String content, @Param("status") String status, Pageable pageable);

    /**
     * Find list question random list.
     *
     * @param questionId the question id
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM question q " +
            "WHERE q.status = N'ACTIVE' AND id = :questionId", nativeQuery = true)
    Question findQuestionWithStatus(Integer questionId);

    /**
     * Find question by ids list.
     *
     * @param questionId the question id
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM question " +
            "WHERE id IN (:questionId)", nativeQuery = true)
    List<Question> findQuestionByIds(List<Integer> questionId);
}
