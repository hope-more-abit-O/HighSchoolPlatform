package com.demo.admissionportal.dto.request.admisison;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateAdmissionRequest {
    private Integer admissionId;
    private Integer year;

    private List<ModifyAdmissionTrainingProgramRequest> modifyAdmissionTrainingPrograms;

    private List<ModifyAdmissionMethodRequest> modifyAdmissionMethods;

    private List<ModifyAdmissionTrainingProgramMethodRequest> modifyAdmissionTrainingProgramMethodRequests;

    private List<ModifyAdmissionTrainingProgramSubjectGroupRequest> modifyAdmissionDetailRequests;
}
