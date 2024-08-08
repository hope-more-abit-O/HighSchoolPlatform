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

    @Query(value = """
SELECT distinct atpm.*
FROM admission_training_program_subject_group atpsg
INNER JOIN dbo.admission_training_program atp on atp.id = atpsg.admission_training_program_id
INNER JOIN admission_training_program_method atpm on atpm.admission_training_program_id = atp.id
INNER JOIN admission_method am on am.id = atpm.admission_method_id
INNER JOIN dbo.admission a on a.id = atp.admission_id
INNER JOIN [user] u on u.id = a.university_id
INNER JOIN university_campus uc on uc.university_id = u.id
INNER JOIN dbo.[major] m ON m.id = atp.major_id
WHERE atpsg.subject_group_id = :subjectGroupId 
AND (am.method_id = 1)
AND (atpm.addmission_score <= (:score + :offset))
AND (m.code like :majorCode)
AND (uc.province_id = :provinceId)
AND (a.year = (:year - 2) OR a.year = (:year - 1) OR a.year = (2024));
""",nativeQuery = true)
    List<AdmissionTrainingProgramMethod> findBySubjectGroupIdAndScoreWithOffset(@Param("subjectGroupId") Integer subjectGroupId,
                                                                                @Param("score") Float score,
                                                                                @Param("offset") Float offset,
                                                                                @Param("majorCode") String majorCode,
                                                                                @Param("provinceId") Integer provinceId,
                                                                                @Param("year") Integer year);
}
