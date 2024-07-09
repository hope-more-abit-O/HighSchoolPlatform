package com.demo.admissionportal.entity.admission;

import com.demo.admissionportal.entity.SubjectGroup;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramSubjectGroupId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admission_training_program_subject_group")
public class AdmissionTrainingProgramSubjectGroup {
    @EmbeddedId
    private AdmissionTrainingProgramSubjectGroupId id;

    @MapsId("admissionTrainingProgramId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admission_training_program_id", nullable = false)
    private AdmissionTrainingProgram admissionTrainingProgram;

    @MapsId("subjectGroupId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_group_id", nullable = false)
    private SubjectGroup subjectGroup;

}