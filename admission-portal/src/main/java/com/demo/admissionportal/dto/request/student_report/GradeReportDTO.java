package com.demo.admissionportal.dto.request.student_report;

import com.demo.admissionportal.constants.GradeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeReportDTO {
    private GradeType grade;
    private List<SemesterMarkDTO> data;
}