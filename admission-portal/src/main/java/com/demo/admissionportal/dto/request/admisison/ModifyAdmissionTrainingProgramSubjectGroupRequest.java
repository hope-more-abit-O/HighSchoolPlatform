package com.demo.admissionportal.dto.request.admisison;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModifyAdmissionTrainingProgramSubjectGroupRequest {
    private Integer admissionTrainingProgramId;
    private Integer subjectGroupIdFrom;
    private Integer subjectGroupIdTo;
}
