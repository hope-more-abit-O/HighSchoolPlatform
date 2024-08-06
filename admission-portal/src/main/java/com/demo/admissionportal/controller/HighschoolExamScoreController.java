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
    public ResponseEntity<ResponseData<Page<HighschoolExamScoreResponse>>> getAllExamScores(
            @RequestParam(required = true) Integer identificationNumber,
            Pageable pageable) {
        ResponseData<Page<HighschoolExamScoreResponse>> response = highschoolExamScoreService.findAllWithFilter(identificationNumber, pageable);
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
    @GetMapping("/A00-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForA00Group() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForA00Group();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/A01-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForA01Group() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForA01Group();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/A02-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForA02Group() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForA02Group();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/A-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForAGroup() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForAGroup();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/B00-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForB00Group() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForB00Group();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/B03-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForB03Group() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForB03Group();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/B08-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForB08Group() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForB08Group();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/B-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForBGroup() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForBGroup();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/C00-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForC00Group() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForC00Group();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/C03-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForC03Group() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForC03Group();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/C04-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForC04Group() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForC04Group();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/C-distribution")
    public ResponseEntity<ResponseData<Map<Float, Integer>>> getScoreDistributionForCGroup() {
        ResponseData<Map<Float, Integer>> response = highschoolExamScoreService.getScoreDistributionForCGroup();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}