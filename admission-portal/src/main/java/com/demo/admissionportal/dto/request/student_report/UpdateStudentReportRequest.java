package com.demo.admissionportal.dto.request.student_report;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Update student report request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentReportRequest {
    @NotBlank(message = "Tên học bạ không được để trống !")
    private String studentReportName;
    @NotEmpty(message = "Danh sách điểm không được để trống !")
    private List<UpdateMarkDTO> marks;
}