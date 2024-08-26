package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Question type repository.
 */
public interface QuestionTypeRepository extends JpaRepository<QuestionType, Integer> {
    /**
     * Find by name question type.
     *
     * @param name the name
     * @return the question type
     */
    QuestionType findByName(String name);
}
