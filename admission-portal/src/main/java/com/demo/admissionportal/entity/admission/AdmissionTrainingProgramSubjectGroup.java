package com.demo.admissionportal.entity.admission;

import com.demo.admissionportal.entity.SubjectGroup;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramSubjectGroupId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admission_training_program_subject_group")
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionTrainingProgramSubjectGroup {
    @EmbeddedId
    private AdmissionTrainingProgramSubjectGroupId id;

    public AdmissionTrainingProgramSubjectGroup(Integer admissionTrainingProgramId, Integer subjectGroupId) {
        this.id = new AdmissionTrainingProgramSubjectGroupId(admissionTrainingProgramId, subjectGroupId);
    }
}