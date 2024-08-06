package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramSubjectGroup;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramSubjectGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AdmissionTrainingProgramSubjectGroupRepository extends JpaRepository<AdmissionTrainingProgramSubjectGroup, AdmissionTrainingProgramSubjectGroupId> {
    List<AdmissionTrainingProgramSubjectGroup> findById_AdmissionTrainingProgramIdIn(Collection<Integer> admissionTrainingProgramIds);
}
