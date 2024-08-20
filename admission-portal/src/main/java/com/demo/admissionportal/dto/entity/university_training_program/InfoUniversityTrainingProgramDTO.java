package com.demo.admissionportal.dto.entity.university_training_program;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoUniversityTrainingProgramDTO {
    private Integer id;
    private Integer universityId;
    private Integer majorId;
    private String trainingSpecific;
    private String language;
    private String status;
}
