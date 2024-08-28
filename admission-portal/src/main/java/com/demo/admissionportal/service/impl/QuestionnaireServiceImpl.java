package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.QuestionStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.holland_test.DeleteQuestionQuestionnaireRequest;
import com.demo.admissionportal.dto.request.holland_test.QuestionCreateRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.QuestionResponse;
import com.demo.admissionportal.dto.response.holland_test.QuestionnaireResponse;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.QuestionQuestionType;
import com.demo.admissionportal.entity.sub_entity.QuestionnaireQuestion;
import com.demo.admissionportal.repository.QuestionRepository;
import com.demo.admissionportal.repository.QuestionnaireRepository;
import com.demo.admissionportal.repository.StaffInfoRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionQuestionTypeRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionTypeRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionnaireQuestionRepository;
import com.demo.admissionportal.service.QuestionService;
import com.demo.admissionportal.service.QuestionnaireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionnaireServiceImpl implements QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final StaffInfoRepository staffInfoRepository;
    private final ModelMapper modelMapper;
    private final QuestionnaireQuestionRepository questionnaireQuestionRepository;
    private final QuestionQuestionTypeRepository questionQuestionTypeRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    @Override
    public ResponseData<Page<QuestionnaireResponse>> getListQuestionnaire(String code, String name, String status, Pageable pageable) {
        Page<Questionnaire> questionnaireList = questionnaireRepository.findQuestionnaire(code, name, status, pageable);
        Page<QuestionnaireResponse> questionnaireResponses = questionnaireList
                .map(questionnaire -> {
                    StaffInfo staffInfo = staffInfoRepository.findStaffInfoById(questionnaire.getCreateBy());
                    int numberOfQuestion = questionnaireQuestionRepository.countByQuestionnaireId(questionnaire.getId());
                    QuestionnaireResponse mappedQuestionnaire = modelMapper.map(questionnaire, QuestionnaireResponse.class);
                    mappedQuestionnaire.setNumberOfQuestions(numberOfQuestion);
                    mappedQuestionnaire.setStatus(questionnaire.getStatus().name);
                    mappedQuestionnaire.setCreateBy(staffInfo.getFirstName() + " " + staffInfo.getMiddleName() + " " + staffInfo.getLastName());
                    return mappedQuestionnaire;
                });
        return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách câu hỏi thành công", questionnaireResponses);
    }

    @Override
    public ResponseData<List<QuestionResponse>> getQuestionnaireById(Integer questionnaireId) {
        try {
            List<QuestionnaireQuestion> questionnaireQuestions = questionnaireQuestionRepository.findByQuestionnaireId(questionnaireId);
            List<QuestionResponse> result = questionnaireQuestions.stream()
                    .map(this::mapToListQuestionnaireResponse)
                    .collect(Collectors.toList());

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách câu hỏi với bộ câu hỏi Id: " + questionnaireId + " thành công", result);
        } catch (Exception ex) {
            log.error("Error when getting questionnaire by ID: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi lấy danh sách câu hỏi", null);
        }
    }

    @Override
    public ResponseData<String> deleteQuestionnaireId(Integer questionnaireId) {
        try {
            Integer updateBy = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (questionnaireId == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "request null");
            }
            Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId).orElse(null);
            if (questionnaire == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy question");
            }
            questionnaire.setStatus(QuestionStatus.INACTIVE);
            questionnaire.setUpdateBy(updateBy);
            questionnaire.setUpdateTime(new Date());
            questionnaireRepository.save(questionnaire);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Xoá questionnaire thành công");
        } catch (Exception ex) {
            log.error("Error when delete questionnaire with ID: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi xoá câu hỏi", null);
        }
    }

    private QuestionResponse mapToListQuestionnaireResponse(QuestionnaireQuestion questionnaireQuestion) {
        Question question = questionRepository.findQuestionWithStatus(questionnaireQuestion.getQuestionId());
        QuestionQuestionType questionQuestionType = questionQuestionTypeRepository.findQuestionQuestionTypeByQuestionTypeIdWithStatus(questionnaireQuestion.getQuestionId());
        QuestionType questionType = questionTypeRepository.findById(questionQuestionType.getQuestionTypeId()).orElse(null);
        return QuestionResponse.builder()
                .questionId(question.getId())
                .questionType(questionType.getName())
                .content(question.getContent())
                .createTime(question.getCreateTime())
                .questionType(questionType.getName())
                .build();
    }

    @Override
    public ResponseData<String> updateQuestionnaire(Integer questionnaireId, List<QuestionCreateRequestDTO> request) {
        try {
            if (questionnaireId == null || request == null || request.isEmpty()) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "request null or empty");
            }
            boolean checkValidate = questionService.checkQuestionIsEnough(request);
            if (!checkValidate) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Question không đủ");
            }
            // Get existing questions in the questionnaire
            List<QuestionnaireQuestion> existingQuestions = questionnaireQuestionRepository.findByQuestionnaireId(questionnaireId);
            Set<Integer> existingQuestionIds = existingQuestions.stream()
                    .map(QuestionnaireQuestion::getQuestionId)
                    .collect(Collectors.toSet());

            // Get new question IDs from the request
            Set<Integer> newQuestionIds = request.stream()
                    .map(QuestionCreateRequestDTO::getQuestionId)
                    .collect(Collectors.toSet());

            // Delete questions that are not in the new request
            for (QuestionnaireQuestion existingQuestion : existingQuestions) {
                if (!newQuestionIds.contains(existingQuestion.getQuestionId())) {
                    questionnaireQuestionRepository.delete(existingQuestion);
                }
            }

            // Add new questions that are not in the existing questions
            for (Integer questionId : newQuestionIds) {
                if (!existingQuestionIds.contains(questionId)) {
                    QuestionnaireQuestion newQuestionnaireQuestion = new QuestionnaireQuestion();
                    newQuestionnaireQuestion.setQuestionId(questionId);
                    newQuestionnaireQuestion.setQuestionnaireId(questionnaireId);
                    newQuestionnaireQuestion.setStatus(QuestionStatus.ACTIVE);
                    questionnaireQuestionRepository.save(newQuestionnaireQuestion);
                }
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật bộ câu hỏi thành công");
        } catch (Exception ex) {
            log.error("Error when updating questionnaire with ID {}: {}", questionnaireId, ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi cập nhật câu hỏi");
        }
    }
}
