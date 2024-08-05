package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AdmissionTrainingProgramMethodRepository extends JpaRepository<AdmissionTrainingProgramMethod, AdmissionTrainingProgramMethodId> {
    List<AdmissionTrainingProgramMethod> findById_AdmissionTrainingProgramIdIn(Collection<Integer> admissionTrainingProgramIds);
}
