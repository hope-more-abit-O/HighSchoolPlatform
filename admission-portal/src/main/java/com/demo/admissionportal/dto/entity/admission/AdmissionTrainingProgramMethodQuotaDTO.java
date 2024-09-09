package com.demo.admissionportal.dto.entity.admission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionTrainingProgramMethodQuotaDTO {
    private Integer admissionTrainingProgramId;
    private Integer admissionMethodId;
    private Integer quota;
    private Float score;
}
