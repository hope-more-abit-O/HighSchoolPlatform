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
public class AdmissionTrainingProgramSubjectGroupId implements Serializable {
    private static final long serialVersionUID = -3712121789938142964L;
    @NotNull
    @Column(name = "admission_training_program_id", nullable = false)
    private Integer admissionTrainingProgramId;

    @NotNull
    @Column(name = "subject_group_id", nullable = false)
    private Integer subjectGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AdmissionTrainingProgramSubjectGroupId entity = (AdmissionTrainingProgramSubjectGroupId) o;
        return Objects.equals(this.admissionTrainingProgramId, entity.admissionTrainingProgramId) &&
                Objects.equals(this.subjectGroupId, entity.subjectGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(admissionTrainingProgramId, subjectGroupId);
    }

}