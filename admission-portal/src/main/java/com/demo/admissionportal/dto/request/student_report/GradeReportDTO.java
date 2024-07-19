package com.demo.admissionportal.dto.request.student_report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Grade report dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeReportDTO {
    private Integer grade;
    private List<SemesterMarkDTO> semesterMarks;
}