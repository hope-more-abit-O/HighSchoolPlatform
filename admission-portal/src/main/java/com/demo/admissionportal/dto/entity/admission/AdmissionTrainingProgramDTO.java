package com.demo.admissionportal.dto.entity.admission;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdmissionTrainingProgramDTO {
    private Integer admissionId;
    private String majorName;
    private String mainSubjectName;
    private String language;
    private String trainingSpecific;
}
