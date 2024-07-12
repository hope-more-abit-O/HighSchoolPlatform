package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.District;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.entity.sub_entity.id.ProvinceDistrictId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "province_district")
public class ProvinceDistrict {
    @EmbeddedId
    private ProvinceDistrictId id;

    @MapsId("provinceId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "province_id")
    private Province province;

    @MapsId("districtId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;

}