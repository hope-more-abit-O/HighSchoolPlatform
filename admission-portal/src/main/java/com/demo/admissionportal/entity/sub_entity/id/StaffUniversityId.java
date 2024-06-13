package com.demo.admissionportal.entity.sub_entity.id;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StaffUniversityId implements Serializable {
    private Integer staffId;
    private Integer universityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffUniversityId that = (StaffUniversityId) o;
        return staffId.equals(that.staffId) && universityId.equals(that.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staffId, universityId);
    }
}
