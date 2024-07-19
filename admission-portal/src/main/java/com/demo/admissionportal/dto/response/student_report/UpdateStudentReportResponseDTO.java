package com.demo.admissionportal.dto.response.student_report;

import com.demo.admissionportal.constants.StudentReportStatus;
import com.demo.admissionportal.dto.request.student_report.SubjectReportDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * The type Update student report response dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentReportResponseDTO {
    private Integer id;
    private Integer studentId;
    private String name;
    private Integer updateBy;
    private Date updateTime;
    private StudentReportStatus status;
    private List<SubjectReportDTO> report;
}
