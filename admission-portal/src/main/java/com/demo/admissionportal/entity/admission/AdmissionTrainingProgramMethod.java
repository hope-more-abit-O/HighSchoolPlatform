package com.demo.admissionportal.entity.admission;

import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admission_training_program_method")
public class AdmissionTrainingProgramMethod {
    @EmbeddedId
    private AdmissionTrainingProgramMethodId id;

    @MapsId("admissionTrainingProgramId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admission_training_program_id", nullable = false)
    private AdmissionTrainingProgram admissionTrainingProgram;

    @MapsId("admissionMethodId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admission_method_id", nullable = false)
    private AdmissionMethod admissionMethod;

    @Column(name = "quota")
    private Integer quota;

    @Column(name = "addmission_score")
    private Double addmissionScore;

}