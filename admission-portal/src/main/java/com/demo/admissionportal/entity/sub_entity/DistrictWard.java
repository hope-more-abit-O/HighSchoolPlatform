package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.sub_entity.id.DistrictWardId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(DistrictWardId.class)
@Table(name = "[district_ward]")
public class DistrictWard {
    @Id
    @Column(name = "district_id")
    private Integer districtId;

    @Id
    @Column(name = "ward_id")
    private Integer wardId;
}