package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmisionTrainingRepostory extends JpaRepository<AdmissionTrainingProgram, Integer> {
}
