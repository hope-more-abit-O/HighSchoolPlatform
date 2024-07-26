package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdmissionQuotaDTO {

    private Integer majorId;
    private String majorName;
    private String majorCode;
    private Integer mainSubjectId;
    private String language;
    private String trainingSpecific;
    private Integer admissionTrainingProgramId;

    private Integer methodId;
    private String methodName;
    private String methodCode;
    private Integer admissionMethodId;

    private List<Integer> subjectGroupIds;

    private Integer quota;

    public AdmissionQuotaDTO(CreateAdmissionQuotaRequest request) {
        this.majorId =request.getMajorId();
        this.majorCode =request.getMajorCode();
        this.majorName =request.getMajorName();
        this.mainSubjectId =request.getMainSubjectId();
        this.language =request.getLanguage();
        this.trainingSpecific =request.getTrainingSpecific();
        this.admissionTrainingProgramId =request.getQuota();
        this.methodId =request.getMethodId();
        this.methodName =request.getMethodName();
        this.methodCode =request.getMethodCode();
        this.admissionMethodId =request.getMethodId();
        this.subjectGroupIds =request.getSubjectGroupIds();
        this.quota =request.getQuota();
    }
}
