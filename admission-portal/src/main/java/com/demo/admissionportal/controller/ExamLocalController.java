package com.demo.admissionportal.controller;

import com.demo.admissionportal.entity.ExamLocal;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.ExamLocalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-local")
@RequiredArgsConstructor
public class ExamLocalController {

    private final ExamLocalService examLocalService;

    @PostMapping
    public ResponseEntity<ResponseData<ExamLocal>> createExamLocal(@RequestBody ExamLocal examLocal) {
        ResponseData<ExamLocal> response = examLocalService.createExamLocal(examLocal);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<ExamLocal>> updateExamLocal(@PathVariable Integer id, @RequestBody ExamLocal examLocal) {
        ResponseData<ExamLocal> response = examLocalService.updateExamLocal(id, examLocal);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<ExamLocal>> getExamLocalById(@PathVariable Integer id) {
        ResponseData<ExamLocal> response = examLocalService.getExamLocalById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseData<List<ExamLocal>>> getAllExamLocals() {
        ResponseData<List<ExamLocal>> response = examLocalService.getAllExamLocals();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}