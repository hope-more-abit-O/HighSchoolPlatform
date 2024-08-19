package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.holland_test.CreateQuestionRequest;
import com.demo.admissionportal.dto.request.holland_test.UpdateQuestionRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.CreateQuestionResponse;
import com.demo.admissionportal.dto.response.holland_test.DeleteQuestionResponse;
import com.demo.admissionportal.dto.response.holland_test.QuestionResponse;
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
    @Autowired
    private QuestionService questionService;

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

    @GetMapping("/question/list")
    public ResponseEntity<ResponseData<List<QuestionResponse>>> getListQuestion() {
        ResponseData<List<QuestionResponse>> resultOfList = questionService.getListQuestion();
        if (resultOfList.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfList);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfList);
    }

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
}
