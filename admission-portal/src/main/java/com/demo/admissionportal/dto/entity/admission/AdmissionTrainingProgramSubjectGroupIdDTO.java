package com.demo.admissionportal.dto.entity.admission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdmissionTrainingProgramSubjectGroupIdDTO {
    private Integer admissionTrainingProgramId;
    private List<Integer> subjectGroupIds;
}
