package com.demo.admissionportal.repository.impl;

import com.demo.admissionportal.dto.entity.subject_report_mark.SubjectMarkDTO;
import com.demo.admissionportal.entity.StudentReport;
import com.demo.admissionportal.repository.StudentReportMarkRepository;
import com.demo.admissionportal.repository.StudentReportRepository;
import com.demo.admissionportal.repository.SubjectGradeSemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentReportMarkServiceImpl {
    private final StudentReportMarkRepository studentReportMarkRepository;
    private final SubjectGradeSemesterRepository subjectGradeSemesterRepository;

}
