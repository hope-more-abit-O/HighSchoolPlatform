package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.HighschoolExamScoreStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "highschool_exam_score")
public class HighschoolExamScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "identification_number")
    private Integer identificationNumber;
    @Column(name = "local")
    private String local;
    @Column(name = "examination_board")
    private String examinationBoard;
    @Column(name = "examiner")
    private String examiner;
    @Column(name = "dob")
    private String dateOfBirth;
    @Column(name = "subject_id")
    private Integer subjectId;
    @Column(name = "score")
    private Float score;
    @Column(name = "year")
    private Integer year;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "update_by")
    private Integer updateBy;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private HighschoolExamScoreStatus status;
}
