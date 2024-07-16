package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.dto.entity.admission.AdmissionTrainingProgramMethodQuotaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionTrainingProgramMethodRequest {
    private Integer admissionId;
    private List<AdmissionTrainingProgramMethodQuotaDTO> quotas;
}
