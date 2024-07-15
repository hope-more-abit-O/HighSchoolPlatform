package com.demo.admissionportal.dto.request.admisison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionTrainingProgramSubjectGroupRequest {
    private Integer admissionTrainingProgramId;
    private List<Integer> subjectGroupId;
}
