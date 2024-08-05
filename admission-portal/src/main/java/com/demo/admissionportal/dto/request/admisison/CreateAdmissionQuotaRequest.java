package com.demo.admissionportal.dto.request.admisison;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionQuotaRequest implements Serializable {
    private Integer majorId;

    private String majorName;
    private String majorCode;

    private Integer mainSubjectId;
    private String language;
    private String trainingSpecific;

    private Integer methodId;

    private String methodName;
    private String methodCode;

    private List<Integer> subjectGroupIds;

    private Integer quota;
}
