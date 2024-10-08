package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainingProgramDTO {
    private Integer majorId;
    private Integer mainSubjectId;
    private String language;
    private String trainingSpecific;
    private Integer quota;

    public TrainingProgramDTO(CreateAdmissionQuotaRequest request) {
        this.majorId = request.getMajorId();
        this.mainSubjectId = request.getMainSubjectId();
        this.language = request.getLanguage();
        this.trainingSpecific = request.getTrainingSpecific();
        this.quota = request.getQuota();
    }

    public TrainingProgramDTO(UpdateAdmissionQuotaDTO request) {
        this.majorId = request.getMajorId();
        this.mainSubjectId = request.getMainSubjectId();
        this.language = request.getLanguage();
        this.trainingSpecific = request.getTrainingSpecific();
        this.quota = request.getQuota();
    }
}
