package com.demo.admissionportal.dto.entity.student_report;

import com.demo.admissionportal.dto.entity.SubjectDTO;
import com.demo.admissionportal.entity.StudentReportHighSchoolScore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStudentReportHighSchoolExamScoreDTO {
    @NotNull
    private Float score;
    @NotNull
    private SubjectDTO subject;


    public GetStudentReportHighSchoolExamScoreDTO(StudentReportHighSchoolScore studentReportHighSchoolScore, SubjectDTO subjectDTO) {
        this.score = studentReportHighSchoolScore == null ? null : studentReportHighSchoolScore.getScore();
        this.subject = subjectDTO;
    }
}
