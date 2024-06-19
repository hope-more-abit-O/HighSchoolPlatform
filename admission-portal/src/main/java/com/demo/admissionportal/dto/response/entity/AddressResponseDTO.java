package com.demo.admissionportal.dto.response.entity;

import com.demo.admissionportal.entity.address.Address;
import com.demo.admissionportal.entity.address.District;
import com.demo.admissionportal.entity.address.Province;
import com.demo.admissionportal.entity.address.Ward;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
    private String specificAddress;
    private String provinceName;
    private String districtName;
    private String wardName;

    public AddressResponseDTO(Address address, Province province, District district, Ward ward) {
        this.specificAddress = address.getSpecificAddress();
        this.provinceName = province.getName();
        this.districtName = district.getName();
        this.wardName = ward.getName();
    }
}
