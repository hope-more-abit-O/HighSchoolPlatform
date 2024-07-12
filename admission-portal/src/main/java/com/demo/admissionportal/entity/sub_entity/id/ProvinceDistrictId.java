package com.demo.admissionportal.entity.sub_entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ProvinceDistrictId implements Serializable {
    private static final long serialVersionUID = 7699717991516447467L;
    @NotNull
    @Column(name = "province_id", nullable = false)
    private Integer provinceId;

    @NotNull
    @Column(name = "district_id", nullable = false)
    private Integer districtId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProvinceDistrictId entity = (ProvinceDistrictId) o;
        return Objects.equals(this.districtId, entity.districtId) &&
                Objects.equals(this.provinceId, entity.provinceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(districtId, provinceId);
    }

}