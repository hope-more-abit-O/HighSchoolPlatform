package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.CreateQuestionRequest;
import com.demo.admissionportal.dto.CreateQuestionResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/holland-test")
@RequiredArgsConstructor
public class HollandQuestionController {
    @Autowired
    private QuestionService questionService;

    @PostMapping
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
}