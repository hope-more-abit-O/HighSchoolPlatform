package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionTrainingProgramRepository extends JpaRepository<AdmissionTrainingProgram, Integer> {


    List<AdmissionTrainingProgram> findByAdmissionId(Integer admissionId);

    @Query(value = """
    SELECT admission_id
    FROM admission_training_program
    WHERE id IN (:admissionTrainingProgramIds)
""", nativeQuery = true)
    List<Integer> findAdmissionIdByAdmissionTrainingProgramIds(@Param("admissionTrainingProgramIds") List<Integer> admissionTrainingProgramIds);

    List<AdmissionTrainingProgram> findByAdmissionIdIn(Collection<Integer> admissionIds);

    List<AdmissionTrainingProgram> findByAdmissionIdInAndMajorIdIn(Collection<Integer> admissionIds, Collection<Integer> majorIds);

    @Query("SELECT DISTINCT atp.majorId FROM AdmissionTrainingProgram atp WHERE atp.admissionId IN " +
            "(SELECT a.id FROM Admission a WHERE a.universityId = :universityId)")
    List<Integer> findMajorIdsByUniversityId(@Param("universityId") Integer universityId);


    Integer deleteByIdIn(List<Integer> ids);
}
