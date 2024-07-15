package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.student_report.CreateStudentReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.StudentReport;
import com.demo.admissionportal.repository.StudentReportRepository;
import com.demo.admissionportal.service.StudentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudentReportServiceImpl implements StudentReportService {
    private final StudentReportRepository studentReportRepository;

    @Override
    public ResponseData<StudentReport> createStudentReport(CreateStudentReportRequest request) {
        try {
            if (!Objects.isNull(request)){
                StudentReport studentReport = studentReportRepository.findByName(request.getName().trim());
                if (!Objects.isNull(studentReport)){
                    return new ResponseData<>(ResponseCode.C204.getCode(), "");
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
