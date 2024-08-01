package com.demo.admissionportal.dto.entity.university_campus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UniversityProperties implements Serializable {
    private String name;
    private String avatar;
    private String address;
}
