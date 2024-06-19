package com.demo.admissionportal.entity.address;

import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "address_detail")
public class AddressDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "province_id", nullable = false)
    private Integer provinceId; // Assuming foreign key relationships

    @NotNull
    @Column(name = "district_id", nullable = false)
    private Integer districtId;

    @NotNull
    @Column(name = "ward_id", nullable = false)
    private Integer wardId;

    public AddressDetail(Integer provinceId, Integer districtId, Integer wardId) {
        this.provinceId = provinceId;
        this.districtId = districtId;
        this.wardId = wardId;
    }
}