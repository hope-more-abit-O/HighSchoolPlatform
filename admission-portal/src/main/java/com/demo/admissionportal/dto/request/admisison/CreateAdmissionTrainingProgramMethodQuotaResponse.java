package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.dto.entity.admission.AdmissionTrainingProgramMethodQuotaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionTrainingProgramMethodQuotaResponse implements Serializable {
    private List<AdmissionTrainingProgramMethodQuotaDTO> quotas;
}
