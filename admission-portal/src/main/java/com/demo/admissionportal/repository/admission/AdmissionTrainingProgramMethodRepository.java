package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AdmissionTrainingProgramMethodRepository extends JpaRepository<AdmissionTrainingProgramMethod, AdmissionTrainingProgramMethodId> {
    List<AdmissionTrainingProgramMethod> findById_AdmissionTrainingProgramIdIn(Collection<Integer> admissionTrainingProgramIds);

    @Query(value = """
    SELECT atpm.*
    FROM [admission_training_program_method] atpm
    RIGHT JOIN [admission_method] am ON am.id = atpm.admission_method_id
    WHERE am.admission_id = :id
""", nativeQuery = true)
    List<AdmissionTrainingProgramMethod> findByAdmissionId(@Param("id") Integer id);

    @Query(value = """
    SELECT atpm.*
    FROM [admission_training_program_method] atpm
    LEFT JOIN [admission_method] am ON am.id = atpm.admission_method_id
    WHERE am.method_id = :methodId AND atpm.admission_training_program_id IN (:admissionTrainingProgramIds)
""",nativeQuery = true)
    List<AdmissionTrainingProgramMethod> findByMethodIdAndAdmissionTrainingProgramIdIn(@Param("methodId") Integer methodId, @Param("admissionTrainingProgramIds") List<Integer> admissionTrainingProgramIds);

}
