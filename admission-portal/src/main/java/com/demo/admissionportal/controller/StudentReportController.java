package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.student_report.CreateStudentReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.student_report.StudentReportResponseDTO;
import com.demo.admissionportal.service.StudentReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student-report")
@AllArgsConstructor
public class StudentReportController {
    private final StudentReportService studentReportService;

    @PostMapping
    public ResponseEntity<ResponseData<StudentReportResponseDTO>> createStudentReport(
            @RequestBody CreateStudentReportRequest request,
            Authentication authentication
    ) {
        ResponseData createdStudentReport = studentReportService.createStudentReport(request, authentication);
        if (createdStudentReport != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudentReport);
        } else {
            return ResponseEntity.status(createdStudentReport.getStatus()).body(createdStudentReport);
        }
    }
}
