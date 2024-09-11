package com.demo.admissionportal.dto.request.admisison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.demo.admissionportal.dto.entity.admission.CreateTrainingProgramRequest;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdmissionTrainingProgramRequest {
    private List<CreateTrainingProgramRequest> createAdmissionTrainingProgramRequests;
    private DeleteAdmissionTrainingProgramRequest deleteAdmissionTrainingPrograms;
    private List<ModifyAdmissionTrainingProgramRequest> modifyAdmissionTrainingPrograms;
}
