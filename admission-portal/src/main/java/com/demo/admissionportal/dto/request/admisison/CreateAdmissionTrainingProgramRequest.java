package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.dto.entity.admission.CreateTrainingProgramRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionTrainingProgramRequest {
    @NotNull
    private Integer majorId;

    private Integer mainSubjectId;

    private String language;

    private String trainingSpecific;
}
