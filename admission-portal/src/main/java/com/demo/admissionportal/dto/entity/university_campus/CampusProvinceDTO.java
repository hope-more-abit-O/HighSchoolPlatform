package com.demo.admissionportal.dto.entity.university_campus;

import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.entity.UniversityCampus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampusProvinceDTO {
    private Integer id;
    private String name;
    private String type;

    public CampusProvinceDTO(Province province, UniversityCampus universityCampus) {
        this.id = province.getId();
        this.name = province.getName();
        this.type = universityCampus.getType().name;
    }
}
