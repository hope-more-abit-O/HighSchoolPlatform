package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionMethodRepository extends JpaRepository<AdmissionMethod, Integer> {
    List<AdmissionMethod> findByAdmissionId(Integer admissionId);

    @Query(value = """
    SELECT admission_id
    FROM admission_method
    WHERE id IN (:admissionMethod)
""", nativeQuery = true)
    List<Integer> findAdmissionIdByAdmissionMethodIds(@Param("admissionMethod") List<Integer> admissionMethodIds);

}
