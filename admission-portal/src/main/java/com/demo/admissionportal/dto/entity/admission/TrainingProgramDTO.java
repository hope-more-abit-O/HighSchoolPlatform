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

    private String majorName;
    private String majorCode;

    private Integer mainSubjectId;
    private String language;
    private String trainingSpecific;

    public TrainingProgramDTO(CreateAdmissionQuotaRequest request) {
        this.majorId = request.getMajorId();
        this.majorName = request.getMajorName();
        this.majorCode = request.getMajorCode();
        this.mainSubjectId = request.getMainSubjectId();
        this.language = request.getLanguage();
        this.trainingSpecific = request.getTrainingSpecific();
    }
}
