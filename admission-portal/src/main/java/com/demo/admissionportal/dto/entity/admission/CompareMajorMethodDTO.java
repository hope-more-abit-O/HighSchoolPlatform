package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.StudentReport;
import com.demo.admissionportal.entity.admission.AdmissionMethod;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompareMajorMethodDTO {
    private InfoMethodDTO method;
    private Integer admissionMethodId;
    private Float score;
    private Integer quota;
    private Boolean isRecommended;

    public CompareMajorMethodDTO(AdmissionMethod admissionMethod, AdmissionTrainingProgramMethod admissionTrainingProgramMethod, StudentReport studentReport, Method method) {
        this.method = new InfoMethodDTO(method);
        this.score = admissionTrainingProgramMethod.getAdmissionScore();
        this.isRecommended = false;
        this.quota = admissionTrainingProgramMethod.getQuota();
        this.admissionMethodId = admissionMethod.getMethodId();
        setRecommend(admissionMethod, studentReport, admissionTrainingProgramMethod);
    }

    public void setRecommend(AdmissionMethod admissionMethod, StudentReport studentReport, AdmissionTrainingProgramMethod admissionTrainingProgramMethod) {
        this.isRecommended = false;

        if (studentReport == null) {
            return;
        }

        if (admissionMethod.getMethodId().equals(1)) {
            if (studentReport.getHighSchoolExamScore() != null && studentReport.getHighSchoolExamScore() >= admissionTrainingProgramMethod.getAdmissionScore()) {
                this.isRecommended = true;
            }
        } else if (admissionMethod.getMethodId().equals(6)) {
            if (studentReport.getCompetencyAssessmentExamScore() != null && studentReport.getCompetencyAssessmentExamScore() >= admissionTrainingProgramMethod.getAdmissionScore()) {
                this.isRecommended = true;
            }
        } else if (admissionMethod.getMethodId().equals(7)) {
            if (studentReport.getCompetencyAssessmentExamScore() != null && studentReport.getCompetencyAssessmentExamScore() >= admissionTrainingProgramMethod.getAdmissionScore()) {
                this.isRecommended = true;
            }
        }
    }
}
