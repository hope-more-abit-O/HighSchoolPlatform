package com.demo.admissionportal.dto.request.student_report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Subject report dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectReportDTO {
    private Integer subjectId;
    private List<GradeReportDTO> grades;
}
