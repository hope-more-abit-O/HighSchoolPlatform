package com.demo.admissionportal.dto.entity.admission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FullAdmissionQuotaDTOV2 {
    private AdmissionMethodDTO method;
    private AdmissionTrainingProgramDTO trainingProgram;
    private Float admissionScore;
    private Integer quota;
}
