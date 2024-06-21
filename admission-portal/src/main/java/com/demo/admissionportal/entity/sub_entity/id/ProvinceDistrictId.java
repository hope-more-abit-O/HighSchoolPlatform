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
public class ProvinceDistrictId implements Serializable {
    private Integer provinceId;
    private Integer districtId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProvinceDistrictId that = (ProvinceDistrictId) o;
        return provinceId.equals(that.provinceId) && districtId.equals(that.districtId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provinceId, districtId);
    }
}
