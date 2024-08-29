package com.demo.admissionportal.dto.entity.university_campus;

import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.entity.UniversityCampus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampusProvinceDTO {
    private Integer id;
    private String name;
    private String type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isChosen;

    public CampusProvinceDTO(Province province, UniversityCampus universityCampus) {
        this.id = province.getId();
        this.name = province.getName();
        this.type = universityCampus.getType().name;
    }

    public CampusProvinceDTO(Province province, UniversityCampus universityCampus, List<Integer> provinceIds) {
        this.id = province.getId();
        this.name = province.getName();
        this.type = universityCampus.getType().name;
        if (provinceIds == null) {
            this.isChosen = false;
            return;
        }
        this.isChosen = provinceIds.contains(province.getId());
    }
}
