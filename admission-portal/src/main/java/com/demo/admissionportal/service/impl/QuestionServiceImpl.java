package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.QuestionStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.holland_test.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.*;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.QuestionJob;
import com.demo.admissionportal.entity.sub_entity.QuestionQuestionType;
import com.demo.admissionportal.entity.sub_entity.QuestionTypeJob;
import com.demo.admissionportal.entity.sub_entity.QuestionnaireQuestion;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.sub_repository.*;
import com.demo.admissionportal.service.QuestionJobService;
import com.demo.admissionportal.service.QuestionQuestionTypeService;
import com.demo.admissionportal.service.QuestionService;
import com.demo.admissionportal.service.ValidationService;
import com.demo.admissionportal.util.impl.RandomCodeHollandTest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionnaireQuestionRepository questionnaireQuestionRepository;
    private final RandomCodeHollandTest randomCodeHollandTest;
    private final TestResponseRepository testResponseRepository;
    private final UserInfoRepository userInfoRepository;
    private final TestResponseAnswerRepository testResponseAnswerRepository;
    private final QuestionTypeJobRepository questionTypeJobRepository;

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
    public ResponseData<Page<QuestionResponse>> getListQuestion(Pageable pageable) {
        try {
            Page<Question> questionList = questionRepository.findListQuestion(pageable);
            Page<QuestionResponse> questionResponses = questionList.map(this::mapToListQuestionResponse);
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

    @Override
    public ResponseData<List<QuestionResponse>> getRandomQuestion() {
        try {
            List<Question> allQuestions = questionRepository.findAll();
            Map<Integer, List<Question>> questionsByType = allQuestions.stream()
                    .collect(Collectors.groupingBy(Question::getType));

            List<QuestionResponse> resultOfRandom = new ArrayList<>();

            questionsByType.forEach((typeId, questions) -> {
                List<Question> randomQuestions = getRandomSubset(questions, 10);
                randomQuestions.forEach(question -> {
                    QuestionResponse questionResponse = mapToListQuestionResponse(question);
                    resultOfRandom.add(questionResponse);
                });
            });

            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy câu hỏi ngẫu nhiên thành công", resultOfRandom);
        } catch (Exception e) {
            log.error("Error while getting random questions: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Failed to fetch random questions", null);
        }
    }

    private List<Question> getRandomSubset(List<Question> questions, int count) {
        Collections.shuffle(questions);
        return questions.stream()
                .limit(count)
                .collect(Collectors.toList());
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseData<QuestionnaireDetailResponse> createQuestionnaire(QuestionnaireRequest request) {
        try {
            Integer staffId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (request == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "request null");
            }
            // Check question is enough 10 with each type
            boolean isEnough = checkQuestionIsEnough(request.getQuestions());
            if (!isEnough) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Question không đủ");
            }
            // Create questionnaire
            Questionnaire questionnaire = modelMapper.map(request, Questionnaire.class);
            questionnaire.setCreateBy(staffId);
            questionnaire.setCreateTime(new Date());
            questionnaire.setStatus(QuestionStatus.ACTIVE);
            questionnaire.setCode(randomCodeHollandTest.generateRandomCode());
            questionnaireRepository.save(questionnaire);

            // Create questionnaire with 60 question
            for (QuestionResponse question : request.getQuestions()) {
                QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
                questionnaireQuestion.setQuestionId(question.getQuestionId());
                questionnaireQuestion.setQuestionnaireId(questionnaire.getId());
                questionnaireQuestionRepository.save(questionnaireQuestion);
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo bộ câu hỏi thành công");
        } catch (Exception ex) {
            log.error("Error while add questionnaire : {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi tạo bộ câu hỏi");
        }
    }

    public boolean checkQuestionIsEnough(List<QuestionResponse> question) {
        // Group questions by their type
        Map<String, List<QuestionResponse>> questionsByType = question.stream()
                .collect(Collectors.groupingBy(QuestionResponse::getQuestionType));

        // Check if all 6 types have exactly 10 questions
        if (questionsByType.size() != 6) {
            return false;
        }
        for (Map.Entry<String, List<QuestionResponse>> entry : questionsByType.entrySet()) {
            if (entry.getValue().size() != 10) {
                return false;
            }
        }
        return true;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseData<ParticipateResponse> getHollandTest() {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            UserInfo userInfo = userInfoRepository.findUserInfoById(userId);
            // Get questionnaire for user participate
            List<Questionnaire> questionnaires = questionnaireRepository.findAll();
            TestResponse result = new TestResponse();
            Collections.shuffle(questionnaires, new Random());
            // Save questionnaire for user
            if (!questionnaires.isEmpty()) {
                Questionnaire selectedQuestionnaire = questionnaires.get(0);
                TestResponse testResponse = new TestResponse();
                testResponse.setQuestionnaireId(selectedQuestionnaire.getId());
                testResponse.setUserId(userInfo.getId());
                testResponse.setCreateTime(new Date());
                result = testResponseRepository.save(testResponse);
            }
            TestResponse testResponse = testResponseRepository.findTestResponseById(result.getId());
            List<QuestionnaireQuestion> questionnaireQuestions = questionnaireQuestionRepository.findByQuestionnaireId(testResponse.getQuestionnaireId());
            ParticipateResponse resultGetHollandTest = new ParticipateResponse();
            resultGetHollandTest.setTestResponseId(testResponse.getId());
            resultGetHollandTest.setQuestion(
                    questionnaireQuestions.stream()
                            .map(this::mapToQuestion)
                            .collect(Collectors.toList())
            );
            Collections.shuffle(resultGetHollandTest.getQuestion(), new Random());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách câu hỏi holland test thành công", resultGetHollandTest);
        } catch (Exception ex) {
            log.error("Error while get holland test : {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi lấy câu hỏi holland test");
        }
    }

    private ParticipateQuestionResponse mapToQuestion(QuestionnaireQuestion questionnaireQuestion) {
        Question question = questionRepository.findById(questionnaireQuestion.getQuestionId()).orElse(null);
        if (question == null) {
            return null;
        }
        return new ParticipateQuestionResponse(question.getId(), question.getContent());
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseData<SubmitResponse> submitHollandTest(SubmitRequestDTO request) {
        try {
            log.info("Start Submit answer holland test");
            TestResponse testResponse = testResponseRepository.findTestResponseById(request.getTestResponseId());
            if (testResponse == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy test response Id");

            }
            request.getQuestion().forEach(submit -> mapToSubmit(submit, testResponse));
            log.info("End Submit answer holland test");
            List<Integer> questionIds = request.getQuestion().stream()
                    .map(SubmitQuestionRequestDTO::getQuestion_id)
                    .collect(Collectors.toList());
            List<Question> questions = questionRepository.findQuestionByIds(questionIds);
            Map<Integer, Integer> questionsCountByType = questions.stream()
                    .collect(Collectors.groupingBy(
                            Question::getType,
                            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                    ));
            List<SubmitDetailResponse> submitDetail = mapToSubmitDetail(questionsCountByType);
            List<HighestTypeResponse> highestTypeResponses = mapToMaxValue(questionsCountByType);
            List<SuggestJobResponse> suggestMajor = mapToMajor(highestTypeResponses);
            SubmitResponse result = SubmitResponse.builder()
                    .submitDetail(submitDetail)
                    .highestType(highestTypeResponses)
                    .suggestJob(suggestMajor)
                    .build();
            return new ResponseData<>(ResponseCode.C200.getCode(), "Kết quả holland test", result);

        } catch (Exception ex) {
            log.error("Error while submit holland test : {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi nộp câu trả lời holland test");
        }
    }

    private List<SuggestJobResponse> mapToMajor(List<HighestTypeResponse> highestTypeResponses) {
        return highestTypeResponses.stream()
                .flatMap(major -> {
                    List<QuestionTypeJob> questionType = questionTypeJobRepository.findByQuestionTypeId(major.getTypeQuestionId());
                    List<Integer> jobIds = questionType.stream()
                            .map(QuestionTypeJob::getJobId)
                            .collect(Collectors.toList());
                    List<Job> jobs = jobRepository.findJobsByIdIn(jobIds);
                    return jobs.stream()
                            .map(job -> SuggestJobResponse.builder()
                                    .jobName(job.getName())
                                    .image(job.getImage())
                                    .build());
                })
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<SubmitDetailResponse> mapToSubmitDetail(Map<Integer, Integer> questionsCountByType) {
        List<String> nameOfCharacter = List.of(
                "Kỹ thuật",
                "Nghiên cứu",
                "Nghệ thuật",
                "Xã hội",
                "Quản lý",
                "Nghiệp vụ"
        );
        List<SubmitDetailResponse> submitDetailResponses = new ArrayList<>();

        for (String character : nameOfCharacter) {
            Integer typeId = mapToTypeQuestionId(character);
            int numberOfSubmit = questionsCountByType.getOrDefault(typeId, 0);
            submitDetailResponses.add(
                    SubmitDetailResponse.builder()
                            .typeQuestions(character)
                            .numberOfSubmit(numberOfSubmit)
                            .build()
            );
        }
        return submitDetailResponses;
    }

    private Integer mapToTypeQuestionId(String character) {
        QuestionType questionType = questionTypeRepository.findByName(character);
        return questionType != null ? questionType.getId() : null;
    }

    private List<HighestTypeResponse> mapToMaxValue(Map<Integer, Integer> questionsCountByType) {
        int maxValue = questionsCountByType.values().stream()
                .max(Integer::compareTo)
                .orElse(0);
        return questionsCountByType.entrySet().stream()
                .filter(entry -> entry.getValue() == maxValue)
                .map(entry -> {
                    QuestionType questionType = questionTypeRepository.findById(entry.getKey()).orElse(null);
                    if (questionType != null) {
                        return HighestTypeResponse.builder()
                                .typeQuestionId(questionType.getId())
                                .typeQuestion(questionType.getName())
                                .content(questionType.getContent())
                                .image(questionType.getImage())
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void mapToSubmit(SubmitQuestionRequestDTO submitRequestDTO, TestResponse testResponse) {
        TestResponseAnswer testResponseAnswer = new TestResponseAnswer();
        testResponseAnswer.setQuestionId(submitRequestDTO.getQuestion_id());
        testResponseAnswer.setTestResponseId(testResponse.getId());
        testResponseAnswerRepository.save(testResponseAnswer);
    }

    public ResponseData<SubmitResponse> getHistoryByTestResponseId(Integer testResponseId) {
        try {
            if (testResponseId == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "testResponseId null");
            }
            List<TestResponseAnswer> responseAnswers = testResponseAnswerRepository.findTestResponseAnswerByTestResponseId(testResponseId);
            List<Integer> questionIds = responseAnswers.stream()
                    .map(TestResponseAnswer::getQuestionId)
                    .collect(Collectors.toList());
            List<Question> questions = questionRepository.findQuestionByIds(questionIds);
            Map<Integer, Integer> questionsCountByType = questions.stream()
                    .collect(Collectors.groupingBy(
                            Question::getType,
                            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                    ));
            List<SubmitDetailResponse> submitDetail = mapToSubmitDetail(questionsCountByType);
            List<HighestTypeResponse> highestTypeResponses = mapToMaxValue(questionsCountByType);
            List<SuggestJobResponse> suggestMajor = mapToMajor(highestTypeResponses);
            SubmitResponse result = SubmitResponse.builder()
                    .submitDetail(submitDetail)
                    .highestType(highestTypeResponses)
                    .suggestJob(suggestMajor)
                    .build();
            return new ResponseData<>(ResponseCode.C200.getCode(), "Kết quả holland test", result);
        } catch (Exception e) {
            log.error("Error while get holland test by test response Id: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi lấy lịch sử holland test với id: " + testResponseId);
        }
    }

    @Override
    public ResponseData<List<HistoryParticipateResponse>> getHistory() {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<TestResponse> listOfTestResponse = testResponseRepository.findTestResponseByUserId(userId);
            listOfTestResponse.stream()
                    .map(this::mapToTestResponse)
                    .collect(Collectors.toList());
            List<HistoryParticipateResponse> response = listOfTestResponse.stream()
                    .map(this::mapToQuestionnaire)
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy lịch sử holland test thành công", response);
        } catch (Exception ex) {
            log.error("Error while get holland test by history: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi lấy lịch sử holland test");
        }
    }

    private HistoryParticipateResponse mapToTestResponse(TestResponse testResponse) {
        return HistoryParticipateResponse.builder()
                .createTime(testResponse.getCreateTime())
                .testResponseId(testResponse.getId())
                .build();
    }

    private HistoryParticipateResponse mapToQuestionnaire(TestResponse testResponse) {
        Questionnaire questionnaire = questionnaireRepository.findById(testResponse.getQuestionnaireId()).orElse(null);
        return HistoryParticipateResponse.builder()
                .code(questionnaire.getCode())
                .createTime(testResponse.getCreateTime())
                .testResponseId(testResponse.getId())
                .name(questionnaire.getName())
                .build();
    }
}
