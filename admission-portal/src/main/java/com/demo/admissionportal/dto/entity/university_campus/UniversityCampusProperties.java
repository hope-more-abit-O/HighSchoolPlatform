package com.demo.admissionportal.dto.entity.university_campus;

import com.demo.admissionportal.dto.request.post.DistrictResponseDTO;
import com.demo.admissionportal.dto.request.post.ProvinceResponseDTO;
import com.demo.admissionportal.dto.request.post.WardResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type University campus properties.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UniversityCampusProperties implements Serializable {
    private Integer id;
    private String campusName;
    private String phone;
    private String email;
    private List<String> picture;
    private String address;
    private String specificAddress;
    private ProvinceResponseDTO provinceResponseDTO;
    private WardResponseDTO wardResponseDTO;
    private DistrictResponseDTO districtResponseDTO;
    private String type;
    private String status;
}
