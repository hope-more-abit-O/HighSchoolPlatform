package com.demo.admissionportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionAnalysisResponse {
    private String advice;
    private String avgDiffMessage;
    private String quotaDiffMessage;
}