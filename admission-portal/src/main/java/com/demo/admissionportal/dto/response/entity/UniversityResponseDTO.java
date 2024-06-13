package com.demo.admissionportal.dto.response.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UniversityResponseDTO {
    private Integer id;
    private String code;
    private String name;
    private String username;
    private String email;
    private String description;
    private String avatar;
    private String type;
}
