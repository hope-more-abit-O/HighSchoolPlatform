package com.demo.admissionportal.entity.sub_entity.id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectGroupSubjectId implements Serializable {
    private Integer subjectId;
    private Integer subjectGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectGroupSubjectId that = (SubjectGroupSubjectId) o;
        return Objects.equals(subjectId, that.subjectId) && Objects.equals(subjectGroupId, that.subjectGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, subjectGroupId);
    }
}
