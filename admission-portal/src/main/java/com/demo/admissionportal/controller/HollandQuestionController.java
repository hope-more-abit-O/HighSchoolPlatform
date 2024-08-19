package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.holland_test.CreateJobRequest;
import com.demo.admissionportal.dto.request.holland_test.CreateQuestionRequest;
import com.demo.admissionportal.dto.request.holland_test.UpdateQuestionRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.*;
import com.demo.admissionportal.service.JobService;
import com.demo.admissionportal.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@PreAuthorize("hasAuthority('STAFF')")

public class HollandQuestionController {
    private final QuestionService questionService;
    private final JobService jobService;

    /**
     * Create question response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/question")
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
     * @return the list question
     */
    @GetMapping("/question/list")
    public ResponseEntity<ResponseData<List<QuestionResponse>>> getListQuestion() {
        ResponseData<List<QuestionResponse>> resultOfList = questionService.getListQuestion();
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
     * @return the list job
     */
    @GetMapping("/job")
    public ResponseEntity<ResponseData<List<JobResponse>>> getListJob() {
        ResponseData<List<JobResponse>> responseJobList = jobService.getAllJob();
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
    public ResponseEntity<ResponseData<List<CreateJobResponse>>> createJob(@RequestBody @Valid List<CreateJobRequest> request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "request null"));
        }
        ResponseData<List<CreateJobResponse>> list = jobService.createJob(request);
        if (list.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } else if (list.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(list);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(list);
    }
}
