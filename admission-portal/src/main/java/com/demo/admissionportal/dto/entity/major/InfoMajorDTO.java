package com.demo.admissionportal.dto.entity.major;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoMajorDTO implements Serializable {
    private Integer id;
    private String name;
    private String code;
}
