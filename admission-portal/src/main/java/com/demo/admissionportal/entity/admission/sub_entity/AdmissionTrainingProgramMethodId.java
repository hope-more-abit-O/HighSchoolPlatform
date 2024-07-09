package com.demo.admissionportal.entity.admission.sub_entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class AdmissionTrainingProgramMethodId implements Serializable {
    private static final long serialVersionUID = 7182383566972044135L;
    @NotNull
    @Column(name = "admission_training_program_id", nullable = false)
    private Integer admissionTrainingProgramId;

    @NotNull
    @Column(name = "admission_method_id", nullable = false)
    private Integer admissionMethodId;

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

}