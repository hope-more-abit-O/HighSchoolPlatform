package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.QuestionStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.holland_test.CreateQuestionRequest;
import com.demo.admissionportal.dto.request.holland_test.UpdateQuestionRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.CreateQuestionResponse;
import com.demo.admissionportal.dto.response.holland_test.DeleteQuestionResponse;
import com.demo.admissionportal.dto.response.holland_test.QuestionResponse;
import com.demo.admissionportal.entity.Job;
import com.demo.admissionportal.entity.Question;
import com.demo.admissionportal.entity.QuestionType;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.sub_entity.PostType;
import com.demo.admissionportal.entity.sub_entity.QuestionJob;
import com.demo.admissionportal.entity.sub_entity.QuestionQuestionType;
import com.demo.admissionportal.repository.JobRepository;
import com.demo.admissionportal.repository.QuestionRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionJobRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionQuestionTypeRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionTypeRepository;
import com.demo.admissionportal.service.QuestionJobService;
import com.demo.admissionportal.service.QuestionQuestionTypeService;
import com.demo.admissionportal.service.QuestionService;
import com.demo.admissionportal.service.ValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Question service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionQuestionTypeRepository questionQuestionTypeRepository;
    private final QuestionJobRepository questionJobRepository;
    private final ModelMapper modelMapper;
    private final QuestionTypeRepository questionTypeRepository;
    private final JobRepository jobRepository;
    private final QuestionJobService questionJobService;
    private final QuestionQuestionTypeService questionQuestionTypeService;

    @Override
    @Transactional
    public ResponseData<CreateQuestionResponse> createQuestion(CreateQuestionRequest request) {
        try {
            if (!Objects.isNull(request)) {
                Question existingQuestion = questionRepository.findByContent(request.getContent().trim());
                if (existingQuestion != null) {
                    log.error("Question with content '{}' already exists", request.getContent());
                    return new ResponseData<>(ResponseCode.C205.getCode(), "Câu hỏi '" + request.getContent() + "' đã tồn tại", null);
                }

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Object principal = authentication.getPrincipal();

                if (!(principal instanceof User)) {
                    return new ResponseData<>(ResponseCode.C205.getCode(), "Người tham chiếu không hợp lệ !");
                }
                Integer staffId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

                Question question = new Question();
                question.setContent(request.getContent().trim());
                question.setType(request.getTypeId());
                question.setCreateBy(staffId);
                question.setCreateTime(new Date());
                question.setStatus(QuestionStatus.ACTIVE);

                List<Job> jobs = new ArrayList<>();
                for (Integer jobId : request.getJobs()) {
                    Optional<Job> jobOptional = jobRepository.findById(jobId);
                    if (jobOptional.isEmpty()) {
                        log.error("Job not found with ID: {}", jobId);
                        return new ResponseData<>(ResponseCode.C203.getCode(), "Công việc không được tìm thấy");
                    }
                    jobs.add(jobOptional.get());
                }
                Question savedQuestion = questionRepository.save(question);

                Optional<QuestionType> questionTypeOptional = questionTypeRepository.findById(request.getTypeId());
                if (questionTypeOptional.isEmpty()) {
                    log.error("Question type with ID '{}' not found", request.getTypeId());
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Loại câu hỏi không được tìm thấy", null);
                }
                QuestionType questionType = questionTypeOptional.get();

                QuestionQuestionType questionQuestionType = new QuestionQuestionType();
                questionQuestionType.setQuestionId(savedQuestion.getId());
                questionQuestionType.setCreateBy(staffId);
                questionQuestionType.setQuestionTypeId(questionType.getId());
                questionQuestionType.setCreateTime(new Date());
                questionQuestionType.setStatus(QuestionStatus.ACTIVE);
                questionQuestionTypeRepository.save(questionQuestionType);

                jobs.stream()
                        .map(job -> {
                            QuestionJob questionJob = new QuestionJob();
                            questionJob.setJobId(job.getId());
                            questionJob.setCreateBy(staffId);
                            questionJob.setQuestionId(savedQuestion.getId());
                            questionJob.setCreateTime(new Date());
                            questionJob.setStatus(QuestionStatus.ACTIVE);
                            return questionJobRepository.save(questionJob);
                        })
                        .toList();

                CreateQuestionResponse response = modelMapper.map(savedQuestion, CreateQuestionResponse.class);
                response.setType(questionType.getName());
                response.setJobNames(jobs.stream()
                        .map(Job::getName)
                        .toList());

                log.info("Question '{}' is successfully added", request.getContent());
                return new ResponseData<>(ResponseCode.C200.getCode(), "Câu hỏi được thêm thành công !", response);
            }
        } catch (Exception ex) {
            log.error("Error occurred while creating question: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Xuất hiện lỗi khi tạo câu hỏi");
        }
        return new ResponseData<>(ResponseCode.C201.getCode(), "Yêu cầu không hợp lệ");
    }

    @Override
    @Transactional
    public ResponseData<DeleteQuestionResponse> deleteQuestion(Integer questionId) {
        try {
            if (questionId == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "questionId null");
            }
            Question questionExisted = questionRepository.findById(questionId).orElse(null);
            if (questionExisted == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy question với Id: " + questionId);
            }
            List<QuestionJob> questionJob = questionJobRepository.findQuestionJobByQuestionId(questionExisted.getId());
            for (QuestionJob qj : questionJob) {
                if (qj.getStatus().equals(QuestionStatus.ACTIVE)) {
                    qj.setStatus(QuestionStatus.INACTIVE);

                } else {
                    qj.setStatus(QuestionStatus.ACTIVE);
                }
                questionJobRepository.save(qj);
            }
            QuestionQuestionType questionType = questionQuestionTypeRepository.findQuestionQuestionTypeByQuestionId(questionExisted.getId());
            if (questionType.getStatus().equals(QuestionStatus.ACTIVE)) {
                questionType.setStatus(QuestionStatus.INACTIVE);
            } else {
                questionType.setStatus(QuestionStatus.ACTIVE);
            }
            questionQuestionTypeRepository.save(questionType);
            if (questionExisted.getStatus().equals(QuestionStatus.ACTIVE)) {
                questionExisted.setStatus(QuestionStatus.INACTIVE);

            } else {
                questionExisted.setStatus(QuestionStatus.ACTIVE);
            }
            questionRepository.save(questionExisted);
            DeleteQuestionResponse response = new DeleteQuestionResponse();
            response.setCurrentStatus(questionExisted.getStatus().name);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật trạng thái question thành công", response);
        } catch (Exception e) {
            log.error("Error while delete question Id: {}", questionId);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi xoá question id: " + questionId);
        }
    }

    @Override
    public ResponseData<List<QuestionResponse>> getListQuestion() {
        try {
            List<Question> questionList = questionRepository.findAll();
            List<QuestionResponse> questionResponses = questionList.stream()
                    .map(this::mapToListQuestionResponse)
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách question thành công", questionResponses);
        } catch (Exception e) {
            log.error("Error while get list question : {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi lấy danh sách question", null);
        }
    }

    private QuestionResponse mapToListQuestionResponse(Question question) {
        QuestionQuestionType questionQuestionType = questionQuestionTypeRepository.findQuestionQuestionTypeByQuestionId(question.getId());
        QuestionType questionType = questionTypeRepository.findById(questionQuestionType.getQuestionTypeId()).orElse(null);
        List<QuestionJob> questionJob = questionJobRepository.findQuestionJobByQuestionId(question.getId());
        String jobNames = questionJob.stream()
                .map(this::mapToJob)
                .map(Job::getName)
                .collect(Collectors.joining(", "));

        return QuestionResponse.builder()
                .questionId(question.getId())
                .questionType(questionType.getName())
                .content(question.getContent())
                .createTime(question.getCreateTime())
                .questionType(questionType.getName())
                .jobName(jobNames)
                .status(question.getStatus().name)
                .build();
    }

    private Job mapToJob(QuestionJob questionJob) {
        return jobRepository.findById(questionJob.getJobId()).orElse(null);
    }

    @Transactional
    @Override
    public ResponseData<String> updateQuestion(Integer questionId, UpdateQuestionRequest request) {
        try {
            Integer staffId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (questionId == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "questionId null");
            }
            Question questionExisted = questionRepository.findById(questionId).orElse(null);
            if (questionExisted == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy question với Id: " + questionId);
            }

            // Update question
            ValidationService.updateIfChanged(request.getContent(), questionExisted.getContent(), questionExisted::setContent);
            ValidationService.updateIfChanged(request.getType(), questionExisted.getType(), questionExisted::setType);
            questionExisted.setUpdateBy(staffId);
            questionExisted.setUpdateTime(new Date());
            questionRepository.save(questionExisted);

            questionJobService.updateQuestionJob(questionId, request, staffId);

            // Update question type
            questionQuestionTypeService.updateQuestionType(request.getType(), questionId, staffId);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật question thành công");
        } catch (Exception ex) {
            log.error("Error while update question : {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi cập nhật question", null);
        }
    }
}
