package com.demo.admissionportal.dto.response.student_report;

import com.demo.admissionportal.constants.StudentReportStatus;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListStudentReportResponse {
    private Integer id;
    private String name;
    private Date createTime;
    private StudentReportStatus status;
}
