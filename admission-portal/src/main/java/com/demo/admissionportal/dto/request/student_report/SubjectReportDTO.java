package com.demo.admissionportal.dto.request.student_report;

import lombok.Data;

import java.util.List;

@Data
public class SubjectReportDTO {
    private Integer subjectId;
    private List<GradeReportDTO> grades;
}
