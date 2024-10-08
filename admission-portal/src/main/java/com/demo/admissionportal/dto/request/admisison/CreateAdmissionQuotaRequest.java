package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.entity.admission.AdmissionMethod;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.util.enum_validator.EnumId;
import com.demo.admissionportal.util.enum_validator.EnumIdList;
import com.demo.admissionportal.util.enum_validator.EnumQuota;
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
    @NotNull(message = "Mã ngành phải không được để trống")
    @EnumId(message = "Mã ngành phải là số và phải lớn hơn 0")
    private Integer majorId;

    private String majorName;
    private String majorCode;

    @EnumId(message = "Mã môn học chính phải là số và phải lớn hơn 0")
    private Integer mainSubjectId;
    private String language;
    private String trainingSpecific;
    private String trainingProgramCode;

    @NotNull(message = "Mã phương thức phải không được để trống")
    @EnumId(message = "Mã phương thức phải là số và phải lớn hơn 0")
    private Integer methodId;
    private String methodName;
    private String methodCode;

    @NotNull(message = "Mã nhóm môn học phải không được để trống")
    @EnumIdList(message = "Mã nhóm môn học phải là số lớn hơn 0 và có ít nhất 1 phần tử")
    private List<Integer> subjectGroupIds;

    @NotNull(message = "Số chỉ tiêu phải không được để trống")
    @EnumQuota(message = "Số chỉ tiêu phải là số và phải lớn hơn 0")
    private Integer quota;

    private Float score;

    public CreateAdmissionQuotaRequest(AdmissionMethod admissionMethod, AdmissionTrainingProgram admissionTrainingProgram, List<Integer> subjectGroupIds, AdmissionTrainingProgramMethod admissionTrainingProgramMethod) {
        this.majorId = admissionTrainingProgram.getMajorId();
        this.mainSubjectId = admissionTrainingProgram.getMainSubjectId();
        this.language = admissionTrainingProgram.getLanguage();
        this.trainingSpecific = admissionTrainingProgram.getTrainingSpecific();
        this.trainingProgramCode = admissionTrainingProgram.getTrainingProgramCode();
        this.methodId = admissionMethod.getMethodId();
        this.subjectGroupIds = subjectGroupIds;
        this.quota = admissionTrainingProgramMethod.getQuota();
        this.score = admissionTrainingProgramMethod.getAdmissionScore();
    }
}
