package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

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

    Integer deleteByIdIn(List<Integer> ids);

    @Query(value = """
select atp.*
from admission_training_program atp
inner join admission a on atp.admission_id = a.id
where a.university_id in (:universityIds) and a.year = :year and atp.major_id = :majorId and a.status = 'ACTIVE'
""", nativeQuery = true)
    List<AdmissionTrainingProgram> findByMajorIdAndUniversityIdsAndYearCustom(@Param("majorId") Integer majorId, @Param("universityIds") List<Integer> universityIds, @Param("year")  Integer year);

    @Query(value = """
SELECT atp.*
FROM admission_training_program atp
INNER JOIN admission a ON atp.admission_id = a.id
WHERE a.year = :year AND atp.major_id = :majorId AND a.status = 'ACTIVE'
""", nativeQuery = true)
    List<AdmissionTrainingProgram> findByMajorIdAndYear(Integer majorId, Integer year);

    @Query("SELECT DISTINCT atp.majorId FROM AdmissionTrainingProgram atp WHERE atp.admissionId IN " +
            "(SELECT a.id FROM Admission a WHERE a.universityId = :universityId)")
    List<Integer> findMajorIdsByUniversityId(@Param("universityId") Integer universityId);
}