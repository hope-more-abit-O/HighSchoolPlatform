package com.demo.admissionportal.dto.request.admisison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdmissionSubjectGroupRequest {
    private List<ModifyAdmissionTrainingProgramSubjectGroupRequest> modifyAdmissionDetailRequests;
    private CreateAdmissionTrainingProgramSubjectGroupRequest createAdmissionDetailRequests;
    private DeleteAdmissionTrainingProgramSubjectGroupRequest deleteAdmissionDetailRequests;
}
