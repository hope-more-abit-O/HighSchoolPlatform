package com.demo.admissionportal.dto.entity.university_campus;

import com.demo.admissionportal.entity.District;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.entity.UniversityCampus;
import com.demo.admissionportal.entity.Ward;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampusProvinceDTO {
    private Integer provinceId;
    private String provinceName;
    private Integer districtId;
    private String districtName;
    private Integer wardId;
    private String wardName;
    private Integer campusId;
    private String campusName;
    private String type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isChosen;

    public CampusProvinceDTO(Province province, UniversityCampus universityCampus) {
        this.provinceId = province.getId();
        this.provinceName = province.getName();
        this.type = universityCampus.getType().name;
    }

    public CampusProvinceDTO(Province province, UniversityCampus universityCampus, List<Integer> provinceIds) {
        this.provinceId = province.getId();
        this.campusId = universityCampus.getId();
        this.campusName = universityCampus.getCampusName();
        this.provinceName = province.getName();
        this.type = universityCampus.getType().name;
        if (provinceIds == null) {
            this.isChosen = false;
            return;
        }
        this.isChosen = provinceIds.contains(province.getId());
    }

    public CampusProvinceDTO(Province province, District district, Ward ward, UniversityCampus universityCampus, List<Integer> provinceIds) {
        this.provinceId = province.getId();
        this.districtId = district.getId();
        this.districtName = district.getName();
        this.wardId = ward.getId();
        this.wardName = ward.getName();
        this.campusId = universityCampus.getId();
        this.campusName = universityCampus.getCampusName();
        this.provinceName = province.getName();
        this.type = universityCampus.getType().name;
        if (provinceIds == null) {
            this.isChosen = false;
            return;
        }
        this.isChosen = provinceIds.contains(province.getId());
    }
}
