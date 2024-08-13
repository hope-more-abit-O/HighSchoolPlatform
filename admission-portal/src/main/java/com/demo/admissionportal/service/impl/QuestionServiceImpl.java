package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.holland_test.CreateQuestionRequest;
import com.demo.admissionportal.dto.response.holland_test.CreateQuestionResponse;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.DeleteQuestionResponse;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.QuestionJob;
import com.demo.admissionportal.entity.sub_entity.QuestionQuestionType;
import com.demo.admissionportal.repository.JobRepository;
import com.demo.admissionportal.repository.QuestionRepository;
import com.demo.admissionportal.repository.StaffInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionJobRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionQuestionTypeRepository;
import com.demo.admissionportal.repository.sub_repository.QuestionTypeRepository;
import com.demo.admissionportal.service.QuestionService;
import com.demo.admissionportal.util.impl.NameUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final UserRepository userRepository;
    private final StaffInfoRepository staffInfoRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final JobRepository jobRepository;

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
                User staff = (User) principal;
                Integer staffId = staff.getId();

                Question question = new Question();
                question.setContent(request.getContent().trim());
                question.setType(request.getTypeId());
                question.setCreateBy(staffId);
                question.setCreateTime(new Date());
                question.setStatus(AccountStatus.ACTIVE);

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
                questionQuestionType.setStatus(AccountStatus.ACTIVE);
                questionQuestionTypeRepository.save(questionQuestionType);

                jobs.stream()
                        .map(job -> {
                            QuestionJob questionJob = new QuestionJob();
                            questionJob.setJobId(job.getId());
                            questionJob.setCreateBy(staffId);
                            questionJob.setQuestionId(savedQuestion.getId());
                            questionJob.setCreateTime(new Date());
                            questionJob.setStatus(AccountStatus.ACTIVE);
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

    private ActionerDTO getStaffDetails(Integer userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    StaffInfo staffInfo = staffInfoRepository.findById(userId).orElse(null);
                    String fullName;
                    if (staffInfo != null) {
                        fullName = NameUtils.getFullName(staffInfo.getFirstName(), staffInfo.getMiddleName(), staffInfo.getLastName());
                    } else {
                        fullName = "Nhân viên UAP";
                    }
                    ActionerDTO actionerDTO = new ActionerDTO(user.getId(), fullName, null, null);
                    actionerDTO.setRole(modelMapper.map(user.getRole(), String.class));
                    actionerDTO.setStatus(modelMapper.map(user.getStatus(), String.class));
                    return actionerDTO;
                }).orElse(null);
    }


    @Override
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
            for (QuestionJob qj : questionJob){
                questionJobRepository.delete(qj);
            }
            questionRepository.delete(questionExisted);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã xoá question với id: " + questionId + "  thành công");
        } catch (Exception e) {
            log.error("Error while delete question Id: {}", questionId);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi xoá question id: " + questionId);
        }
    }
}