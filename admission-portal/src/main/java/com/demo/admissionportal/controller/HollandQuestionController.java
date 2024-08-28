package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.holland_test.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.*;
import com.demo.admissionportal.service.JobService;
import com.demo.admissionportal.service.QuestionService;
import com.demo.admissionportal.service.QuestionnaireService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Holland question controller.
 */
@RestController
@RequestMapping("/api/v1/holland-test")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class HollandQuestionController {
    private final QuestionService questionService;
    private final JobService jobService;
    private final QuestionnaireService questionnaireService;

    /**
     * Create question response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/question")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<CreateQuestionResponse>> createQuestion(@RequestBody @Valid CreateQuestionRequest request) {
        ResponseData<CreateQuestionResponse> createdQuestionResponse = questionService.createQuestion(request);
        if (createdQuestionResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(createdQuestionResponse);
        } else if (createdQuestionResponse.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createdQuestionResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createdQuestionResponse);
    }

    /**
     * Delete question response entity.
     *
     * @param questionId the question id
     * @return the response entity
     */
    @PostMapping("/question/change-status/{questionId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<DeleteQuestionResponse>> deleteQuestion(@PathVariable(name = "questionId") Integer questionId) {
        if (questionId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Question Id null"));
        }
        ResponseData<DeleteQuestionResponse> resultOfDelete = questionService.deleteQuestion(questionId);
        if (resultOfDelete.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfDelete);
        } else if (resultOfDelete.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultOfDelete);
        } else if (resultOfDelete.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultOfDelete);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfDelete);
    }

    /**
     * Gets list question.
     *
     * @param content  the content
     * @param status   the status
     * @param pageable the pageable
     * @return the list question
     */
    @GetMapping("/question/list")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<QuestionResponse>>> getListQuestion(@RequestParam(required = false) String content,
                                                                                @RequestParam(required = false) String status,
                                                                                @PageableDefault(size = 60) Pageable pageable) {
        ResponseData<Page<QuestionResponse>> resultOfList = questionService.getListQuestion(content, status, pageable);
        if (resultOfList.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfList);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfList);
    }

    /**
     * Gets question.
     *
     * @param questionId the question id
     * @param request    the request
     * @return the question
     */
    @PutMapping("/question/{questionId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<String>> getQuestion(@PathVariable(name = "questionId") Integer questionId, @RequestBody @Valid UpdateQuestionRequest request) {
        if (questionId == null || request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Question Id null"));
        }
        ResponseData<String> questionResponse = questionService.updateQuestion(questionId, request);
        if (questionResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(questionResponse);
        } else if (questionResponse.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(questionResponse);
        } else if (questionResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(questionResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(questionResponse);
    }

    /**
     * Gets list job.
     *
     * @param jobName  the job name
     * @param status   the status
     * @param pageable the pageable
     * @return the list job
     */
    @GetMapping("/job")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<JobResponse>>> getListJob(@RequestParam(required = false) String jobName,
                                                                      @RequestParam(required = false) String status,
                                                                      @PageableDefault(size = 60) Pageable pageable) {
        ResponseData<Page<JobResponse>> responseJobList = jobService.getAllJob(jobName, status, pageable);
        if (responseJobList.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseJobList);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseJobList);
    }

    /**
     * Create job response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/job")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<List<CreateJobResponse>>> createJob(@RequestBody @Valid List<CreateJobRequest> request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Request is null"));
        }
        ResponseData<List<CreateJobResponse>> list = jobService.createJob(request);
        if (list.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } else if (list.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(list);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(list);
    }

    /**
     * Delete job response entity.
     *
     * @param jobId the job id
     * @return the response entity
     */
    @DeleteMapping("/job/{jobId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<String>> deleteJob(@PathVariable(name = "jobId") Integer jobId) {
        if (jobId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "jobId null"));
        }
        ResponseData<String> job = jobService.deleteJob(jobId);
        if (job.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(job);
        } else if (job.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(job);
        } else if (job.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(job);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(job);
    }

    /**
     * Gets list random question.
     *
     * @return the list random question
     */
    @GetMapping("/questionnaire/random-question")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<List<QuestionResponse>>> getListRandomQuestion() {
        ResponseData<List<QuestionResponse>> randomQuestion = questionService.getRandomQuestion();
        if (randomQuestion.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(randomQuestion);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(randomQuestion);
    }

    /**
     * Create questionare response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/questionnaire")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<QuestionnaireDetailResponse>> createQuestionnaire(@RequestBody @Valid QuestionnaireRequest request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "request null"));
        }
        ResponseData<QuestionnaireDetailResponse> questionnaireResponse = questionService.createQuestionnaire(request);
        if (questionnaireResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(questionnaireResponse);
        } else if (questionnaireResponse.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(questionnaireResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(questionnaireResponse);
    }

    /**
     * Gets list questionnaire.
     *
     * @param code     the code
     * @param name     the name
     * @param status   the status
     * @param pageable the pageable
     * @return the list questionnaire
     */
    @GetMapping("/questionnaire")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<QuestionnaireResponse>>> getListQuestionnaire(@RequestParam(name = "code", required = false) String code,
                                                                                          @RequestParam(name = "name", required = false) String name,
                                                                                          @RequestParam(name = "status", required = false) String status, Pageable pageable) {
        ResponseData<Page<QuestionnaireResponse>> responseData = questionnaireService.getListQuestionnaire(code, name, status, pageable);
        if (responseData.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
    }

    /**
     * Gets questionnaire.
     *
     * @param questionnaireId the questionnaire id
     * @return the questionnaire
     */
    @GetMapping("/questionnaire/{questionnaireId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<List<QuestionResponse>>> getQuestionnaire(@PathVariable(name = "questionnaireId") Integer questionnaireId) {
        if (questionnaireId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "questionnaireId null"));
        }
        ResponseData<List<QuestionResponse>> getQuestionnaireById = questionnaireService.getQuestionnaireById(questionnaireId);
        if (getQuestionnaireById.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(getQuestionnaireById);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getQuestionnaireById);
    }

    /**
     * Delete question from questionnaire response entity.
     *
     * @param questionnaireId the questionnaire id
     * @return the response entity
     */
    @DeleteMapping("/questionnaire/{questionnaireId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<String>> deleteQuestionFromQuestionnaire(@PathVariable(name = "questionnaireId") Integer questionnaireId) {
        if (questionnaireId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "request null"));
        }
        ResponseData<String> resultOfDelete = questionnaireService.deleteQuestionnaireId(questionnaireId);
        if (resultOfDelete.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfDelete);
        } else if (resultOfDelete.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultOfDelete);
        } else if (resultOfDelete.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultOfDelete);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfDelete);
    }

    /**
     * Gets holland test.
     *
     * @return the holland test
     */
    @GetMapping("/participate")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<ParticipateResponse>> getHollandTest() {
        ResponseData<ParticipateResponse> responseData = questionService.getHollandTest();
        if (responseData.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
    }

    /**
     * Submit holland test response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/submit")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<SubmitResponse>> submitHollandTest(@RequestBody @Valid SubmitRequestDTO request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "request null"));
        }
        ResponseData<SubmitResponse> submitData = questionService.submitHollandTest(request);
        if (submitData.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(submitData);
        } else if (submitData.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(submitData);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(submitData);
    }

    /**
     * Gets history by test response id.
     *
     * @param testResponseId the test response id
     * @return the history by test response id
     */
    @GetMapping("/history/{testResponseId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<SubmitResponse>> getHistoryByTestResponseId(@PathVariable(name = "testResponseId") Integer testResponseId) {
        if (testResponseId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "testResponseId null"));
        }
        ResponseData<SubmitResponse> resultOfHistory = questionService.getHistoryByTestResponseId(testResponseId);
        if (resultOfHistory.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfHistory);
        } else if (resultOfHistory.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultOfHistory);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfHistory);
    }

    /**
     * Gets history.
     *
     * @return the history
     */
    @GetMapping("/history")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<List<HistoryParticipateResponse>>> getHistory() {
        ResponseData<List<HistoryParticipateResponse>> resultOfHistory = questionService.getHistory();
        if (resultOfHistory.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfHistory);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfHistory);
    }

    /**
     * Update questionnaire response entity.
     *
     * @param questionnaireId the questionnaire id
     * @param request         the request
     * @return the response entity
     */
    @PutMapping("/questionnaire/{questionnaireId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<String>> updateQuestionnaire(@PathVariable(name = "questionnaireId") Integer questionnaireId, @RequestBody @Valid List<QuestionCreateRequestDTO> request) {
        if (questionnaireId == null || request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "request null"));
        }
        ResponseData<String> resultOfUpdate = questionnaireService.updateQuestionnaire(questionnaireId, request);
        if (resultOfUpdate.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfUpdate);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfUpdate);
    }

    /**
     * Gets list job.
     *
     * @return the list job
     */
    @GetMapping("/job/list")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<List<JobResponse>>> getListJob() {
        ResponseData<List<JobResponse>> jobList = jobService.getListJob();
        if (jobList.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(jobList);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jobList);
    }


    /**
     * Gets list question no pageable.
     *
     * @return the list question no pageable
     */
    @GetMapping("/question/list/v2")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<List<QuestionResponse>>> getListQuestionNoPageable() {
        ResponseData<List<QuestionResponse>> resultOfList = questionService.getListQuestionV2();
        if (resultOfList.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfList);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfList);
    }
}
