package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramSubjectGroup;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramSubjectGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionTrainingProgramSubjectGroupRepository extends JpaRepository<AdmissionTrainingProgramSubjectGroup, AdmissionTrainingProgramSubjectGroupId> {
}
