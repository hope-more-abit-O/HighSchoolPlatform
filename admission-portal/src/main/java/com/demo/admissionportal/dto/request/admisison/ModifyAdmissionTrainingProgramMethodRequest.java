package com.demo.admissionportal.dto.request.admisison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyAdmissionTrainingProgramMethodRequest {
    private Integer admissionTrainingProgramId;
    private Integer admissionMethodId;
    private Integer quota;
    private Float score;
}
