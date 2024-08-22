package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.holland_test.DeleteQuestionQuestionnaireRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.QuestionResponse;
import com.demo.admissionportal.dto.response.holland_test.QuestionnaireResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Questionnaire service.
 */
public interface QuestionnaireService {
    /**
     * Gets list questionnaire.
     *
     * @param code     the code
     * @param name     the name
     * @param status   the status
     * @param pageable the pageable
     * @return the list questionnaire
     */
    ResponseData<Page<QuestionnaireResponse>> getListQuestionnaire(String code, String name, String status, Pageable pageable);

    /**
     * Gets questionnaire by id.
     *
     * @param questionnaireId the questionnaire id
     * @return the questionnaire by id
     */
    ResponseData<List<QuestionResponse>> getQuestionnaireById(Integer questionnaireId);

    /**
     * Delete question from questionnaire id response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<String> deleteQuestionFromQuestionnaireId(DeleteQuestionQuestionnaireRequest request);
}
