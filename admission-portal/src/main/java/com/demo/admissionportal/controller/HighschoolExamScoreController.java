package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.CreateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.request.UpdateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.response.HighschoolExamScoreResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.HighschoolExamScoreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/exam-scores")
public class HighschoolExamScoreController {

    @Autowired
    private HighschoolExamScoreService highschoolExamScoreService;

    @GetMapping
    public ResponseEntity<ResponseData<List<HighschoolExamScoreResponse>>> getAllExamScores(
            @RequestParam(required = true) Integer identificationNumber) {
        ResponseData<List<HighschoolExamScoreResponse>> response = highschoolExamScoreService.findAllWithFilter(identificationNumber);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<HighschoolExamScoreResponse>> createExamScore(@RequestBody CreateHighschoolExamScoreRequest request) {
        ResponseData<HighschoolExamScoreResponse> response = highschoolExamScoreService.createExamScore(request);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @PutMapping("/{identificationNumber}")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<HighschoolExamScoreResponse>> updateExamScore(
            @PathVariable Integer identificationNumber,
            @RequestBody UpdateHighschoolExamScoreRequest request) {
        ResponseData<HighschoolExamScoreResponse> response = highschoolExamScoreService.updateExamScore(identificationNumber, request);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    //Subject Distribution
    //Subject Group distribution
    @GetMapping("/A-distribution")
    public ResponseEntity<ResponseData<Map<String, Map<Float, Integer>>>> getScoreDistributionForAGroup(
            @RequestParam(required = false) String subjectGroup) {
        ResponseData<Map<String, Map<Float, Integer>>> response = highschoolExamScoreService.getScoreDistributionForAGroup(subjectGroup);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/B-distribution")
    public ResponseEntity<ResponseData<Map<String, Map<Float, Integer>>>> getScoreDistributionForBGroup(
            @RequestParam(required = false) String subjectGroup
    ) {
        ResponseData<Map<String, Map<Float, Integer>>> response = highschoolExamScoreService.getScoreDistributionForBGroup(subjectGroup);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/C-distribution")
    public ResponseEntity<ResponseData<Map<String, Map<Float, Integer>>>> getScoreDistributionForCGroup(
            @RequestParam(required = false) String subjectGroup
    ) {
        ResponseData<Map<String, Map<Float, Integer>>> response = highschoolExamScoreService.getScoreDistributionForCGroup(subjectGroup);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}