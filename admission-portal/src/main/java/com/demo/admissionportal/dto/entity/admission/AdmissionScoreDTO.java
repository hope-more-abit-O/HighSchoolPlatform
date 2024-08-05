package com.demo.admissionportal.dto.entity.admission;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdmissionScoreDTO {
    private Integer admissionTrainingProgramId;
    private Integer admissionMethodId;
    private Float admissionScore;
}
