package com.demo.admissionportal.service;

/**
 * The interface Question type service.
 */
public interface QuestionQuestionTypeService {
    /**
     * Update question type.
     *
     * @param questionTypeId the question type id
     * @param questionId     the question id
     * @param staffId        the staff id
     */
    void updateQuestionType(Integer questionTypeId, Integer questionId, Integer staffId);
}
