package com.demo.admissionportal.dto.response.student_report;

import com.demo.admissionportal.entity.sub_entity.StudentReportMark;

import java.util.List;

public record StudentReportResponse(Integer id, Integer studentId, String name, List<StudentReportMark> marks) {
}
