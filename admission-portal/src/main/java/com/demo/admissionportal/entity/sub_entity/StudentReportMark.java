package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.sub_entity.id.StudentReportMarkId;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(StudentReportMarkId.class)
@Table(name = "student_report_mark")
public class StudentReportMark {
    @Id
    @Column(name = "student_report_id")
    private Integer studentReportId;
    @Id
    @Column(name = "subject_grade_semester_id")
    private Integer subjectGradeSemesterId;
    @Column(name = "mark")
    private Float mark;
}
