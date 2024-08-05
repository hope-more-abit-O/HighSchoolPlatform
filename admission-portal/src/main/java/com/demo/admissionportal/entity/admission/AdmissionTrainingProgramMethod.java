package com.demo.admissionportal.entity.admission;

import com.demo.admissionportal.dto.entity.admission.AdmissionScoreDTO;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admission_training_program_method")
@AllArgsConstructor
@NoArgsConstructor
public class AdmissionTrainingProgramMethod {
    @EmbeddedId
    private AdmissionTrainingProgramMethodId id;

    @Column(name = "quota")
    private Integer quota;

    @Column(name = "addmission_score")
    private Float admissionScore;

    public AdmissionTrainingProgramMethod(Integer admissionTrainingProgramId, Integer admissionMethodId, Integer quota) {
        this.id = new AdmissionTrainingProgramMethodId(admissionTrainingProgramId, admissionMethodId);
        this.quota = quota;
    }

    public AdmissionTrainingProgramMethod(AdmissionScoreDTO admissionScoreDTO) {
        this.id = new AdmissionTrainingProgramMethodId(admissionScoreDTO.getAdmissionTrainingProgramId(), admissionScoreDTO.getAdmissionMethodId());
        this.admissionScore = admissionScoreDTO.getAdmissionScore();
    }
}