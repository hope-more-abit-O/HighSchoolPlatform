package com.demo.admissionportal.dto.request.student_report;

import com.demo.admissionportal.dto.entity.student_report.StudentReportHighSchoolExamScoreDTO;
import com.demo.admissionportal.util.enum_validator.EnumScore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHighSchoolExamScoreForStudentReportRequest {
    @NotNull(message = "Danh sách điểm không được trống.")
    private List<StudentReportHighSchoolExamScoreDTO> scores;
}
