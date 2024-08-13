package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.holland_test.CreateQuestionRequest;
import com.demo.admissionportal.dto.response.holland_test.CreateQuestionResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.DeleteQuestionResponse;

/**
 * The interface Question service.
 */
public interface QuestionService {
    /**
     * Create question response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<CreateQuestionResponse> createQuestion(CreateQuestionRequest request);

    /**
     * Delete question response data.
     *
     * @param questionId the question id
     * @return the response data
     */
    ResponseData<DeleteQuestionResponse> deleteQuestion(Integer questionId);
}
