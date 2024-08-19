package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.holland_test.UpdateQuestionRequest;

/**
 * The interface Question job service.
 */
public interface QuestionJobService {
    /**
     * Update question job.
     *
     * @param questionId the question id
     * @param request    the request
     * @param staffId    the staff id
     */
    void updateQuestionJob(Integer questionId, UpdateQuestionRequest request, Integer staffId);
}
