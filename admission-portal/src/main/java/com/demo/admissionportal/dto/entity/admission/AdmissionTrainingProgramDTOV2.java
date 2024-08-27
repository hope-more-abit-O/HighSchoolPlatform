package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.admission.Admission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdmissionTrainingProgramDTOV2 {
    @JsonIgnore
    private Integer universityId;
    private InfoMajorDTO major;
    private String trainingSpecific;
    private String language;
    private List<AdmissionTrainingProgramScoreDTO> score;

    public AdmissionTrainingProgramDTOV2(Admission admission, InfoMajorDTO major, List<AdmissionTrainingProgramScoreDTO> score) {
        this.universityId = admission.getUniversityId();
        this.major = major;
        this.score = score;
    }

    public AdmissionTrainingProgramDTOV2(Admission admission, InfoMajorDTO major, List<AdmissionTrainingProgramScoreDTO> score, String trainingSpecific, String language) {
        this.universityId = admission.getUniversityId();
        this.major = major;
        this.score = score;
        this.trainingSpecific = trainingSpecific;
        this.language = language;
    }
}
