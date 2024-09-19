package com.demo.admissionportal.dto.request.admisison;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAdmissionTrainingProgramSubjectGroupRequest {
    private List<AdmissionTrainingProgramSubjectGroupIdDTO> admissionTrainingProgramSubjectGroups;
}
