package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.QuestionStatus;
import com.demo.admissionportal.entity.sub_entity.QuestionQuestionType;
import com.demo.admissionportal.repository.sub_repository.QuestionQuestionTypeRepository;
import com.demo.admissionportal.service.QuestionQuestionTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * The type Question type service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionTypeServiceImpl implements QuestionQuestionTypeService {
    private final QuestionQuestionTypeRepository questionQuestionTypeRepository;

    @Override
    public void updateQuestionType(Integer questionTypeId, Integer questionId, Integer staffId) {
        QuestionQuestionType existingEntity = questionQuestionTypeRepository.findQuestionQuestionTypeByQuestionId(questionId);
        if (existingEntity != null) {
            questionQuestionTypeRepository.delete(existingEntity);
        }
        QuestionQuestionType newEntity = new QuestionQuestionType();
        newEntity.setQuestionId(questionId);
        newEntity.setQuestionTypeId(questionTypeId);
        newEntity.setCreateBy(staffId);
        newEntity.setCreateTime(new Date());
        newEntity.setStatus(QuestionStatus.ACTIVE);
        questionQuestionTypeRepository.save(newEntity);
    }
}