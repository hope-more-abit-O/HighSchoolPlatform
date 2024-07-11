package com.demo.admissionportal.dto.entity.admission;

import lombok.Data;

@Data
public class AdmissionTrainingProgramMethodScoreDTO {
    private Integer admissionTrainingProgramId;
    private Integer admissionMethodId;
    private Float score;
}
