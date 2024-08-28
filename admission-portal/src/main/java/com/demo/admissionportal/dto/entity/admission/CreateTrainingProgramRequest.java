package com.demo.admissionportal.dto.entity.admission;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainingProgramRequest {
    @NotBlank
    private Integer majorId;

    private Integer mainSubjectId;

    private String language;

    private String trainingSpecific;
}
