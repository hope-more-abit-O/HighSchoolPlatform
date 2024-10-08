package com.demo.admissionportal.dto.response.student_report;

import com.demo.admissionportal.constants.StudentReportStatus;
import com.demo.admissionportal.dto.entity.student_report.GetHighSchoolExamSubjectScoreDTO;
import com.demo.admissionportal.dto.entity.student_report.GetStudentReportHighSchoolExamScoreDTO;
import com.demo.admissionportal.dto.entity.student_report.StudentReportHighSchoolExamScoreDTO;
import com.demo.admissionportal.dto.request.student_report.SubjectReportDTO;
import com.demo.admissionportal.dto.response.subject.GetHighSchoolExamSubjectsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentReportResponseDTO {
    private Integer id;
    private Integer studentId;
    private String name;
    private Integer createBy;
    private Date createTime;
    private Integer updateBy;
    private Date updateTime;
    private StudentReportStatus status;
    private List<SubjectReportDTO> report;
    private Float competencyAssessmentExamScore;;
    private GetHighSchoolExamSubjectScoreDTO highSchoolExamSubjectScores;
}
