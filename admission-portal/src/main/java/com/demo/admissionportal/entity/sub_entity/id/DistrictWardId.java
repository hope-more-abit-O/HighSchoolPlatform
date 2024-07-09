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
public class DistrictWardId implements Serializable {
    private static final long serialVersionUID = 6053930313302589780L;
    @NotNull
    @Column(name = "district_id", nullable = false)
    private Integer districtId;

    @NotNull
    @Column(name = "ward_id", nullable = false)
    private Integer wardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DistrictWardId entity = (DistrictWardId) o;
        return Objects.equals(this.districtId, entity.districtId) &&
                Objects.equals(this.wardId, entity.wardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(districtId, wardId);
    }

}