package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.holland_test.CreateQuestionRequest;
import com.demo.admissionportal.dto.request.holland_test.QuestionnaireRequest;
import com.demo.admissionportal.dto.request.holland_test.SubmitRequestDTO;
import com.demo.admissionportal.dto.request.holland_test.UpdateQuestionRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @param content  the content
     * @param status   the status
     * @param pageable the pageable
     * @return the list question
     */
    ResponseData<Page<QuestionResponse>> getListQuestion(String content, String status, Pageable pageable);

    /**
     * Update question question reponse.
     *
     * @param questionId the question id
     * @param request    the request
     * @return the question reponse
     */
    ResponseData<String> updateQuestion(Integer questionId, UpdateQuestionRequest request);

    /**
     * Gets random question.
     *
     * @return the random question
     */
    ResponseData<List<QuestionResponse>> getRandomQuestion();

    /**
     * Create questionare response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<QuestionnaireDetailResponse> createQuestionnaire(QuestionnaireRequest request);


    /**
     * Gets holland test.
     *
     * @return the holland test
     */
    ResponseData<ParticipateResponse> getHollandTest();

    /**
     * Submit holland test response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<SubmitResponse> submitHollandTest(SubmitRequestDTO request);

    /**
     * Gets history by test response id.
     *
     * @param testResponseId the test response id
     * @return the history by test response id
     */
    ResponseData<SubmitResponse> getHistoryByTestResponseId(Integer testResponseId);

    /**
     * Gets history.
     *
     * @return the history
     */
    ResponseData<List<HistoryParticipateResponse>> getHistory();
}
