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
    private String identificationNumber;
    @Column(name = "subject_id")
    private Integer subjectId;
    @Column(name = "score")
    private Float score;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_local_id", nullable = false)
    private ExamLocal examLocal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_year_id", nullable = false)
    private ExamYear examYear;
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
