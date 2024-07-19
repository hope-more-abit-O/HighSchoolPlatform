package com.demo.admissionportal.dto.request.student_report;

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
    private String studentReportName;
    private List<UpdateMarkDTO> marks;
}