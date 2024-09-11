package com.demo.admissionportal.dto.request.admisison;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateAdmissionRequest {
    private Integer year;

    private UpdateAdmissionTrainingProgramRequest updateAdmissionTrainingProgramRequest;
    private UpdateAdmissionMethodRequest updateAdmissionMethodRequest;
    private UpdateAdmissionTrainingMethodRequest updateAdmissionTrainingMethodRequest;
    private UpdateAdmissionSubjectGroupRequest updateAdmissionSubjectGroupRequest;
}
