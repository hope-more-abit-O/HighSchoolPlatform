package com.demo.admissionportal.dto.entity.admission;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FullAdmissionQuotaDTO {
    private Integer admissionTrainingProgram;
    private Integer admissionMethod;
    private Integer quota;
    private Float admissionScore;
}
