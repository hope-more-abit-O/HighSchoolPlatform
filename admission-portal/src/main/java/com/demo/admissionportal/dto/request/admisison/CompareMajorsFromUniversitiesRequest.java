package com.demo.admissionportal.dto.request.admisison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompareMajorsFromUniversitiesRequest {
    private Integer universityId;
    private Integer majorId;
}
