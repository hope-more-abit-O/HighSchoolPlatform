package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
