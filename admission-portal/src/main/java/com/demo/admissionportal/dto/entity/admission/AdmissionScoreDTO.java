package com.demo.admissionportal.dto.entity.admission;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdmissionScoreDTO {
    @NotNull
    private Integer admissionTrainingProgramId;
    @NotNull
    private Integer admissionMethodId;
    @NotNull
    private Float admissionScore;
}
