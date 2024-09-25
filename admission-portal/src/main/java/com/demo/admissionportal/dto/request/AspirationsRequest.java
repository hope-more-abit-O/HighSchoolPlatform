package com.demo.admissionportal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AspirationsRequest {

    private List<AdmissionAnalysisRequest> aspirations;
}