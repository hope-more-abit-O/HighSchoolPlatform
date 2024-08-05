package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.dto.entity.admission.AdmissionScoreDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class UpdateAdmissionScoreRequest {
    @NotNull
    private List<AdmissionScoreDTO> admissionScores;
}
