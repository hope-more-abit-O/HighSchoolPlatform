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
/**
 * Data Transfer Object (DTO) representing a detailed address response.
 *
 * This DTO encapsulates the specific address details along with
 * the names of the associated province, district, and ward.
 */
public class AddressResponseDTO {
    private String specificAddress;
    private String provinceName;
    private String districtName;
    private String wardName;

    /**
     * Constructs an AddressResponseDTO with address details.
     *
     * @param address  The Address object containing the specific address.
     * @param province The Province object providing the province/city name.
     * @param district The District object providing the district/town name.
     * @param ward     The Ward object providing the ward/commune name.
     */
    public AddressResponseDTO(Address address, Province province, District district, Ward ward) {
        this.specificAddress = address.getSpecificAddress();
        this.provinceName = province.getName();
        this.districtName = district.getName();
        this.wardName = ward.getName();
    }
}