package com.demo.admissionportal.dto.request.admisison;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionQuotaRequest {
    private Integer majorId;
    private String majorName;
    private String majorCode;
    private Integer mainSubjectId;
    private String language;
    private String trainingSpecific;


    private Integer methodId;
    private String methodCode;
    private String methodName;


    private Integer quota;
}
