package com.demo.admissionportal.entity.sub_entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class StudentReportHighSchoolScoreId implements Serializable {
    private static final long serialVersionUID = 6053930313302589781L;
    @NotNull
    @Column(name = "student_report_id", nullable = false)
    private Integer studentReportId;
    @NotNull
    @Column(name = "subject_id", nullable = false)
    private Integer subjectId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentReportHighSchoolScoreId that = (StudentReportHighSchoolScoreId) o;
        return Objects.equals(studentReportId, that.studentReportId) && Objects.equals(subjectId, that.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentReportId, subjectId);
    }

    public StudentReportHighSchoolScoreId(Integer studentReportId, Integer subjectId) {
        this.studentReportId = studentReportId;
        this.subjectId = subjectId;
    }

    public StudentReportHighSchoolScoreId(){}
}
