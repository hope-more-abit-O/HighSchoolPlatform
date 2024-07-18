package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.student_report.CreateStudentReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.student_report.StudentReportResponseDTO;
import com.demo.admissionportal.entity.StudentReport;
import org.springframework.security.core.Authentication;

public interface StudentReportService {
    ResponseData<StudentReportResponseDTO> createStudentReport(CreateStudentReportRequest request, Authentication authentication);
}
