package com.demo.admissionportal.dto.request.student_report;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateStudentReportRequest {
    @NotBlank(message = "Tên học bạ không được để trống !")
    private String studentReportName;
    private List<SubjectReportDTO> report;
}
