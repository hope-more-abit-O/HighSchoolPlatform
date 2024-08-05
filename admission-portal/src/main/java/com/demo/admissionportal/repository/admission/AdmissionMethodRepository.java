package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionMethodRepository extends JpaRepository<AdmissionMethod, Integer> {
    List<AdmissionMethod> findByAdmissionId(Integer admissionId);
}
