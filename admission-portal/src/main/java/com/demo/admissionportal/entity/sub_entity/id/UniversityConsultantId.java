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
public class UniversityConsultantId implements Serializable {
    private Integer universityId;
    private Integer consultantId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniversityConsultantId that = (UniversityConsultantId) o;
        return consultantId.equals(that.consultantId) && universityId.equals(that.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(universityId, consultantId);
    }
}
