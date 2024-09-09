package com.demo.admissionportal.dto.request.admisison;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdmissionTrainingMethodRequest {
    private List<ModifyAdmissionTrainingProgramMethodRequest> modifyAdmissionTrainingProgramMethodRequests;
    private CreateAdmissionTrainingProgramMethodRequest createAdmissionTrainingProgramMethodRequests;
    private DeleteAdmissionTrainingProgramMethodRequest deleteAdmissionTrainingProgramRequests;
}
