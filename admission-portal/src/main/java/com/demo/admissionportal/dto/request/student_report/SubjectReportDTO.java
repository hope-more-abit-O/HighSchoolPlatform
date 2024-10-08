package com.demo.admissionportal.dto.request.student_report;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Integer subjectId;
    @NotNull
    private String subjectName;
    @NotNull
    private List<GradeReportDTO> grades;
}
