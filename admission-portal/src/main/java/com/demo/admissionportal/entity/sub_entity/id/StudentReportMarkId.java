package com.demo.admissionportal.entity.sub_entity.id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentReportMarkId {
    private Integer studentReportId;
    private Integer subjectGradeSemesterId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentReportMarkId that = (StudentReportMarkId) o;
        return Objects.equals(studentReportId, that.studentReportId) && Objects.equals(subjectGradeSemesterId, that.subjectGradeSemesterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentReportId, subjectGradeSemesterId);
    }
}
