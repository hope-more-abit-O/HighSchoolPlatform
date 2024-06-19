package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.sub_entity.id.ProvinceDistrictId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProvinceDistrictId.class)
@Table(name = "[province_district]")
public class ProvinceDistrict {
    @Id
    @Column(name = "province_id")
    private Integer provinceId;

    @Id
    @Column(name = "district_id")
    private Integer districtId;

}
