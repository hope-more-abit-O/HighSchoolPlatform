package com.demo.admissionportal.dto.request.student_report;

import com.demo.admissionportal.constants.GradeType;
import lombok.Data;

import java.util.List;

@Data
public class GradeReportDTO {
    private GradeType grade;
    private List<SemesterMarkDTO> data;
}