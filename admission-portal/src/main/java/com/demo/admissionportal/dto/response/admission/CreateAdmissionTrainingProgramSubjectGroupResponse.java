package com.demo.admissionportal.dto.response.admission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAdmissionTrainingProgramSubjectGroupResponse {
    private Integer admissionTrainingProgramId;
    private List<Integer> subjectGroupIds;
}
