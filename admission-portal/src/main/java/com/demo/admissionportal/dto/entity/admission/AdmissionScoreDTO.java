package com.demo.admissionportal.dto.entity.admission;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdmissionScoreDTO {
    @NotBlank
    private Integer admissionTrainingProgramId;
    @NotBlank
    private Integer admissionMethodId;
    @NotBlank
    private Float admissionScore;
}
