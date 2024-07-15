package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.admission.FullAdmissionDTO;
import com.demo.admissionportal.dto.entity.admission.CreateTrainingProgramRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionTrainingProgramResponse {
    private FullAdmissionDTO admission;
    private List<CreateTrainingProgramRequest> trainingPrograms;
}
