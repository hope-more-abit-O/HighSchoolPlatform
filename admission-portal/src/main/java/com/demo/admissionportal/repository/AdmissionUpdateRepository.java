package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.AdmissionUpdateStatus;
import com.demo.admissionportal.entity.admission.AdmissionUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionUpdateRepository extends JpaRepository<AdmissionUpdate, Integer> {
    List<AdmissionUpdate> findByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, AdmissionUpdateStatus status);

    Optional<AdmissionUpdate> findFirstByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, AdmissionUpdateStatus status);

    List<AdmissionUpdate> findByBeforeAdmissionId(Integer beforeAdmissionId);

    @Query(value = """
SELECT TOP(1) *
FROM admission_update
WHERE before_admission_id = :beforeAdmissionId
  AND (status = :status
  OR status = :status2)
ORDER BY id DESC
""", nativeQuery = true)
    Optional<AdmissionUpdate> findFirstByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, String status, String status2);
}
