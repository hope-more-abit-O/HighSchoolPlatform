package com.demo.admissionportal.dto.request.student_report;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private Integer subjectId;
    @NotBlank
    private String subjectName;
    @NotBlank
    private List<GradeReportDTO> grades;
}
