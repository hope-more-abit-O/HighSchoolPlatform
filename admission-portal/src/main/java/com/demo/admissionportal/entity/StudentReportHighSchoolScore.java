package com.demo.admissionportal.entity;

import com.demo.admissionportal.dto.entity.student_report.StudentReportHighSchoolExamScoreDTO;
import com.demo.admissionportal.entity.sub_entity.id.StudentReportHighSchoolScoreId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_report_high_school_score")
public class StudentReportHighSchoolScore {
    @EmbeddedId
    private StudentReportHighSchoolScoreId id;
    @Column(name = "score")
    private Float score;

    public StudentReportHighSchoolScore(Integer studentId, StudentReportHighSchoolExamScoreDTO score) {
        this.id = new StudentReportHighSchoolScoreId(studentId, score.getSubjectId());
        this.score = score.getScore();
    }
}
