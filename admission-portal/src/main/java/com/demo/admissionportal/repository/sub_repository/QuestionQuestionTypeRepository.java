package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.QuestionQuestionType;
import com.demo.admissionportal.entity.sub_entity.id.QuestionQuestionTypeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Question question type repository.
 */
@Repository
public interface QuestionQuestionTypeRepository extends JpaRepository<QuestionQuestionType, QuestionQuestionTypeId> {
    /**
     * Find question question type by question id question question type.
     *
     * @param id the id
     * @return the question question type
     */
    QuestionQuestionType findQuestionQuestionTypeByQuestionId(Integer id);

    @Query(value = "SELECT * " +
            "FROM question_question_type " +
            "WHERE status = 'ACTIVE' AND question_id = :questionId", nativeQuery = true)
    QuestionQuestionType findQuestionQuestionTypeByQuestionTypeIdWithStatus(Integer questionId);
}
