package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.holland_test.CreateQuestionRequest;
import com.demo.admissionportal.dto.request.holland_test.UpdateQuestionRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.CreateQuestionResponse;
import com.demo.admissionportal.dto.response.holland_test.DeleteQuestionResponse;
import com.demo.admissionportal.dto.response.holland_test.QuestionResponse;

import java.util.List;

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

    /**
     * Gets list question.
     *
     * @return the list question
     */
    ResponseData<List<QuestionResponse>> getListQuestion();

    /**
     * Update question question reponse.
     *
     * @param questionId the question id
     * @param request    the request
     * @return the question reponse
     */
//    ResponseData<String> updateQuestion(Integer questionId, UpdateQuestionRequest request);
}
