package com.demo.admissionportal.dto.request.student_report;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentReportRequest {
    @NotNull(message = "Tên học bạ không được để trống")
    private String name;
}
