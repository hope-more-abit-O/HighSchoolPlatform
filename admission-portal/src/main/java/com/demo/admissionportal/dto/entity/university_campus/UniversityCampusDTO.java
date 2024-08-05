package com.demo.admissionportal.dto.entity.university_campus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type University campus dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversityCampusDTO implements Serializable {
    private UniversityProperties university;
    private List<UniversityCampusProperties> campus;
}
