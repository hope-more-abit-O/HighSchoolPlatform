package com.demo.admissionportal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionAnalysisRequest {
    private Float score;
    private String subjectGroup;
    private String university;
    private String major;
}
