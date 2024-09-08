package com.demo.admissionportal.dto;

import com.demo.admissionportal.dto.request.AdmissionAnalysisRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aspiration {
    private List<AdmissionAnalysisRequest> aspirations;
}
