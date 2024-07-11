package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.District;
import com.demo.admissionportal.entity.Ward;
import com.demo.admissionportal.entity.sub_entity.id.DistrictWardId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "district_ward")
public class DistrictWard {
    @EmbeddedId
    private DistrictWardId id;

    @MapsId("districtId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @MapsId("wardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    private Ward ward;

}