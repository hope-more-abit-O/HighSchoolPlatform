package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.CreateQuestionRequest;
import com.demo.admissionportal.dto.CreateQuestionResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Question;
import com.demo.admissionportal.entity.sub_entity.QuestionQuestionType;

import java.util.List;

public interface QuestionService {
    ResponseData<CreateQuestionResponse> createQuestion(CreateQuestionRequest request);
}
