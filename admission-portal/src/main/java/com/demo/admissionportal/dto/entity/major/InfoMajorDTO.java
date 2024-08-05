package com.demo.admissionportal.dto.entity.major;

import com.demo.admissionportal.entity.Major;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoMajorDTO {
    private Integer id;
    private String name;
    private String code;

    public InfoMajorDTO(Major major) {
        this.id = major.getId();
        this.name = major.getName();
        this.code = major.getCode();
    }
}
