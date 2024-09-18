package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.StudentReportStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "[student_report]")
public class StudentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "name")
    private String name;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "update_by")
    private Integer updateBy;
    @Column(name = "create_time")
    private Date createTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StudentReportStatus status;
    @Column(name = "high_school_exam_score")
    private Float highSchoolExamScore;
    @Column(name = "competency_assessment_exam_score")
    private Float competencyAssessmentExamScore;
}