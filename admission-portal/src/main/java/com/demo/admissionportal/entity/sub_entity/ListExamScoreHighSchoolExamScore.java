package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.sub_entity.id.ListExamScoreHighSchoolExamScoreId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ListExamScoreHighSchoolExamScoreId.class)
@Table(name = "list_exam_score_high_school_exam_score")
public class ListExamScoreHighSchoolExamScore {
    @Id
    @Column(name = "list_exam_score_by_year_id")
    private Integer listExamScoreByYearId;
    @Id
    @Column(name = "highschool_exam_score_id")
    private Integer highschoolExamScoreId;
    @Column(name = "status")
    private String status;
}
