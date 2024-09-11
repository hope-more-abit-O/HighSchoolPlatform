package com.demo.admissionportal.entity.admission.sub_entity;

import com.demo.admissionportal.dto.entity.admission.AdmissionScoreDTO;
import com.demo.admissionportal.dto.request.admisison.DeleteAdmissionTrainingProgramMethodIdDTO;
import com.demo.admissionportal.dto.request.admisison.UpdateAdmissionScoreRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AdmissionTrainingProgramMethodId implements Serializable {

    @NotNull
    @Column(name = "admission_training_program_id", nullable = false)
    private Integer admissionTrainingProgramId;

    @NotNull
    @Column(name = "admission_method_id", nullable = false)
    private Integer admissionMethodId;

    public AdmissionTrainingProgramMethodId(DeleteAdmissionTrainingProgramMethodIdDTO deleteAdmissionTrainingProgramMethodIdDTO) {
        this.admissionTrainingProgramId = deleteAdmissionTrainingProgramMethodIdDTO.getAdmissionTrainingProgramId();
        this.admissionMethodId = deleteAdmissionTrainingProgramMethodIdDTO.getAdmissionMethodId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AdmissionTrainingProgramMethodId entity = (AdmissionTrainingProgramMethodId) o;
        return Objects.equals(this.admissionTrainingProgramId, entity.admissionTrainingProgramId) &&
                Objects.equals(this.admissionMethodId, entity.admissionMethodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(admissionTrainingProgramId, admissionMethodId);
    }

    public AdmissionTrainingProgramMethodId(AdmissionScoreDTO admissionScoreDTO) {
        this.admissionTrainingProgramId = admissionScoreDTO.getAdmissionTrainingProgramId();
        this.admissionMethodId = admissionScoreDTO.getAdmissionMethodId();
    }

    public String toAObject(){
        return "(" + this.admissionTrainingProgramId + "," + this.admissionMethodId + ")";
    }
}