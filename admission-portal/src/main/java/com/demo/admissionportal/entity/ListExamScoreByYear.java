package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.ListExamScoreByYearStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "list_exam_score_by_year")
public class ListExamScoreByYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "year")
    private Integer year;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ListExamScoreByYearStatus status;
}
