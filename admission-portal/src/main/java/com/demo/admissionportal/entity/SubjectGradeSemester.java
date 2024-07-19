package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.SemesterType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "[subject_grade_semester]")
public class SubjectGradeSemester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "subject_id")
    private int subjectId;
    @Column(name = "grade")
    private Integer grade;
    @Enumerated(EnumType.STRING)
    @Column(name = "semester")
    private SemesterType semester;
}
