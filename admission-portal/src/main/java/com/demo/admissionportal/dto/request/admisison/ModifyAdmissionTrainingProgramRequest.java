package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModifyAdmissionTrainingProgramRequest {
    @NotNull
    private Integer admissionTrainingProgramId;
    private Integer mainSubjectId;
    private String trainingSpecific;
    private String language;
    private Integer quota;

    public boolean isModified(AdmissionTrainingProgram admissionTrainingProgram) {
        boolean isModified = false;
        if ((mainSubjectId == null && admissionTrainingProgram.getMainSubjectId() != null) || (mainSubjectId != null && admissionTrainingProgram.getMainSubjectId() == null) || (mainSubjectId != null && !mainSubjectId.equals(admissionTrainingProgram.getMainSubjectId()))) {
            admissionTrainingProgram.setMainSubjectId(mainSubjectId);
            isModified = true;
        }

        if ((trainingSpecific == null && admissionTrainingProgram.getTrainingSpecific() != null) || (trainingSpecific != null && admissionTrainingProgram.getTrainingSpecific() == null || (trainingSpecific != null && !trainingSpecific.equals(admissionTrainingProgram.getTrainingSpecific())))) {
            admissionTrainingProgram.setTrainingSpecific(trainingSpecific);
            isModified = true;
        }

        if ((language == null && admissionTrainingProgram.getLanguage() != null) || (language != null && admissionTrainingProgram.getLanguage() == null) || (language != null && !language.equals(admissionTrainingProgram.getLanguage()))) {
            admissionTrainingProgram.setLanguage(language);
            isModified = true;
        }

        if ((quota == null && admissionTrainingProgram.getQuota() != null) || (quota != null && admissionTrainingProgram.getQuota() == null) || (quota != null && !quota.equals(admissionTrainingProgram.getQuota()))) {
            admissionTrainingProgram.setQuota(quota);
            isModified = true;
        }

        return isModified;
    }

    public boolean idIsEqual(AdmissionTrainingProgram admissionTrainingProgram) {
        return admissionTrainingProgram.getId().equals(admissionTrainingProgramId);
    }
}
