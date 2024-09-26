package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.SubjectGroupInfoDTO;
import com.demo.admissionportal.dto.SubjectInfDTO;
import com.demo.admissionportal.dto.entity.SubjectDTO;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.StudentReport;
import com.demo.admissionportal.entity.admission.AdmissionMethod;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SubjectGroupResponseDTO2> availableSubjects;

    public CompareMajorMethodDTO(AdmissionMethod admissionMethod, AdmissionTrainingProgramMethod admissionTrainingProgramMethod, StudentReport studentReport, Method method) {
        this.method = new InfoMethodDTO(method);
        this.score = admissionTrainingProgramMethod.getAdmissionScore();
        this.isRecommended = false;
        this.quota = admissionTrainingProgramMethod.getQuota();
        this.admissionMethodId = admissionMethod.getMethodId();
        setRecommend(admissionMethod, studentReport, admissionTrainingProgramMethod);
    }

    public CompareMajorMethodDTO(AdmissionMethod admissionMethod, AdmissionTrainingProgramMethod admissionTrainingProgramMethod, Boolean isRecommended, List<SubjectGroupResponseDTO2> availableSubjects, Method method) {
        this.method = new InfoMethodDTO(method);
        this.score = admissionTrainingProgramMethod.getAdmissionScore();
        this.isRecommended = false;
        this.quota = admissionTrainingProgramMethod.getQuota();
        this.admissionMethodId = admissionMethod.getMethodId();
        this.isRecommended = isRecommended;
        this.availableSubjects = availableSubjects;
    }

    public void setRecommend(AdmissionMethod admissionMethod, StudentReport studentReport, AdmissionTrainingProgramMethod admissionTrainingProgramMethod) {
        this.isRecommended = false;

        if (studentReport == null || admissionTrainingProgramMethod.getAdmissionScore() == null) {
            return;
        }

        if (admissionMethod.getMethodId().equals(6)) {
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
