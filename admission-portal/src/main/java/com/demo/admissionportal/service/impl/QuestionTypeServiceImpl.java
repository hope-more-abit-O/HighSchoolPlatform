package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.sub_entity.QuestionQuestionType;
import com.demo.admissionportal.repository.sub_repository.QuestionQuestionTypeRepository;
import com.demo.admissionportal.service.QuestionQuestionTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type Question type service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionTypeServiceImpl implements QuestionQuestionTypeService {
    private final QuestionQuestionTypeRepository questionQuestionTypeRepository;

    @Override
    public void updateQuestionType(Integer questionId) {
        QuestionQuestionType questionQuestionTypeExisted = questionQuestionTypeRepository.findQuestionQuestionTypeByQuestionId(questionId);

    }
}
