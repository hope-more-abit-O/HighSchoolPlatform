package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.QuestionnaireQuestion;
import com.demo.admissionportal.entity.sub_entity.id.QuestionnaireQuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The interface Questionnaire question repository.
 */
public interface QuestionnaireQuestionRepository extends JpaRepository<QuestionnaireQuestion, QuestionnaireQuestionId> {
    /**
     * Count by questionnaire id int.
     *
     * @param questionnaireId the questionnaire id
     * @return the int
     */
    int countByQuestionnaireId(Integer questionnaireId);

    /**
     * Find by questionnaire id list.
     *
     * @param questionnaireId the questionnaire id
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM questionnaire_question " +
            "WHERE status = 'ACTIVE' AND questionnaire_id = :questionnaireId", nativeQuery = true)
    List<QuestionnaireQuestion> findByQuestionnaireId(Integer questionnaireId);

    /**
     * Find by questionnaire question id questionnaire question.
     *
     * @param questionnaireId the questionnaire id
     * @param questionId      the question id
     * @return the questionnaire question
     */
    @Query(value = "SELECT * " +
            "FROM questionnaire_question " +
            "WHERE status = 'ACTIVE' AND questionnaire_id = :questionnaireId AND question_id = :questionId", nativeQuery = true)
    QuestionnaireQuestion findByQuestionnaireQuestionId(Integer questionnaireId, Integer questionId);
}
