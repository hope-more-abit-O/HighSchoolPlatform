package com.demo.admissionportal.controller;

import com.demo.admissionportal.entity.ExamYear;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.ExamYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-year")
@RequiredArgsConstructor
public class ExamYearController {

    private final ExamYearService examYearService;

    @PostMapping
    public ResponseEntity<ResponseData<ExamYear>> createExamYear(@RequestBody ExamYear examYear) {
        ResponseData<ExamYear> response = examYearService.createExamYear(examYear);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<ExamYear>> updateExamYear(@PathVariable Integer id, @RequestBody ExamYear examYear) {
        ResponseData<ExamYear> response = examYearService.updateExamYear(id, examYear);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<ExamYear>> getExamYearById(@PathVariable Integer id) {
        ResponseData<ExamYear> response = examYearService.getExamYearById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseData<List<ExamYear>>> getAllExamYears() {
        ResponseData<List<ExamYear>> response = examYearService.getAllExamYears();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}