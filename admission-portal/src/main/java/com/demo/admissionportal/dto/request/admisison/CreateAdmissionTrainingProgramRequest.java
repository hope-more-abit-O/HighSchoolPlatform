package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.dto.entity.admission.CreateTrainingProgramRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionTrainingProgramRequest {
    private Integer admissionId;
    private List<CreateTrainingProgramRequest> trainingPrograms;
}
