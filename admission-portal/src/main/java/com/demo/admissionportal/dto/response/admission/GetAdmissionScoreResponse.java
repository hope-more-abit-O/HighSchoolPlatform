package com.demo.admissionportal.dto.response.admission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAdmissionScoreResponse {
    private List<AdmissionScoreWithSubjectGroupDTO> admissionScores;
}
