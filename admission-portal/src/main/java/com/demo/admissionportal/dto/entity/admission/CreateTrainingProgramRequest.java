package com.demo.admissionportal.dto.entity.admission;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainingProgramRequest {
    @NotNull
    private Integer majorId;

    private Integer mainSubjectId;

    private String language;

    private String trainingSpecific;
}
