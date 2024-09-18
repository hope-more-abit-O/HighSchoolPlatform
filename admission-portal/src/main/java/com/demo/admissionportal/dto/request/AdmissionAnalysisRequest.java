package com.demo.admissionportal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionAnalysisRequest {
    private String identificationNumber;
    private Integer subjectGroup;
    private Integer university;
    private Integer major;
}
