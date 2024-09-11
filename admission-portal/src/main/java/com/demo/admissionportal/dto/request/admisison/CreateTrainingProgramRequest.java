package com.demo.admissionportal.dto.request.admisison;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainingProgramRequest {
    private Integer majorId;
    private Integer mainSubjectId;
    private String language;
    private String trainingSpecific;
}
