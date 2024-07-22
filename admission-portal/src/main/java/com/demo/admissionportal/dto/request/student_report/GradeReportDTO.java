package com.demo.admissionportal.dto.request.student_report;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Integer grade;
    @NotNull
    private List<SemesterMarkDTO> semesterMarks;
}