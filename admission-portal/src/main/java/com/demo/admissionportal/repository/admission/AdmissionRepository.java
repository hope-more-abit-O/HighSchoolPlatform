package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.admission.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Integer> {
}
