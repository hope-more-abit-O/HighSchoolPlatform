package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.dto.AdviceResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionAnalysisResponse {
    private List<AdviceResult> results;
}