package com.demo.admissionportal.dto.response.student_report;

import com.demo.admissionportal.constants.StudentReportStatus;
import com.demo.admissionportal.dto.request.student_report.SubjectReportDTO;
import com.demo.admissionportal.entity.sub_entity.StudentReportMark;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * The type Student report response dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentReportResponseDTO {
    private Integer id;
    private Integer studentId;
    private String name;
    private Integer createBy;
    private Date createTime;
    private StudentReportStatus status;
    private List<SubjectReportDTO> report;
}