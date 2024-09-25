package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.AdmissionUpdateStatus;
import com.demo.admissionportal.entity.admission.AdmissionUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionUpdateRepository extends JpaRepository<AdmissionUpdate, Integer> {
    List<AdmissionUpdate> findByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, AdmissionUpdateStatus status);

    Optional<AdmissionUpdate> findFirstByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, AdmissionUpdateStatus status);
}
