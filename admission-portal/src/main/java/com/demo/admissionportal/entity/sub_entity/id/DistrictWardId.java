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
public class DistrictWardId implements Serializable {
    private Integer districtId;
    private Integer wardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistrictWardId that = (DistrictWardId) o;
        return districtId.equals(that.districtId) && wardId.equals(that.wardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(districtId, wardId);
    }
}