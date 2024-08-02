package com.demo.admissionportal.dto.entity.university_campus;

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
    private String type;
    private String status;
}