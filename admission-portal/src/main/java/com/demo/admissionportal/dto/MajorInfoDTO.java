package com.demo.admissionportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MajorInfoDTO {
    private Integer id;
    private String name;
    private String code;
}
