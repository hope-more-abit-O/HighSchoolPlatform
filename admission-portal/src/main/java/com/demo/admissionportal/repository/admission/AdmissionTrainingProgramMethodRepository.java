package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionTrainingProgramMethodRepository extends JpaRepository<AdmissionTrainingProgramMethod, AdmissionTrainingProgramMethodId> {
    List<AdmissionTrainingProgramMethod> findById_AdmissionTrainingProgramIdIn(Collection<Integer> admissionTrainingProgramIds);

    @Query(value = """
    SELECT atpm.*
    FROM [admission_training_program_method] atpm
    INNER JOIN [admission_method] am ON am.id = atpm.admission_method_id
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

    @Query(value = """
    SELECT COUNT(*) FROM [admission_training_program_method]
    """, nativeQuery = true)
    Integer countAll();

    @Query(value = """
SELECT atpm.*
FROM [admission_training_program_method] atpm
INNER JOIN [admission_training_program] atp ON atp.id = atpm.admission_training_program_id
INNER JOIN [admission] a ON a.id = atp.admission_id
WHERE a.university_id = :id AND a.year = :year AND a.status = :admissionStatus
""", nativeQuery = true)
    List<AdmissionTrainingProgramMethod> findByUniversityIdAndYearAndStatus(Integer id, int year, String admissionStatus);

    @Query(value = """
SELECT atpm.*
FROM [admission_training_program_method] atpm
INNER JOIN [admission_training_program] atp ON atp.id = atpm.admission_training_program_id
INNER JOIN [admission] a ON a.id = atp.admission_id
WHERE a.university_id in (:id) AND a.year = :year AND a.status = :admissionStatus
""", nativeQuery = true)
    List<AdmissionTrainingProgramMethod> findByUniversityIdAndYearAndStatus(List<Integer> id, int year, String admissionStatus);

    @Query(value = """
SELECT atpm.*
FROM [admission_training_program_method] atpm
INNER JOIN [admission_method] am ON am.id = atpm.admission_method_id
WHERE am.method_id in (:methodIds)
AND atpm.admission_training_program_id in (:admissionTrainingProgramIds)
""", nativeQuery = true)
    List<AdmissionTrainingProgramMethod> findByAdmissionTrainingProgramIdInAndMethodIds(List<Integer> admissionTrainingProgramIds, List<Integer> methodIds);

    List<AdmissionTrainingProgramMethod> findById_AdmissionTrainingProgramIdInAndId_AdmissionMethodIdIn(Collection<Integer> admissionTrainingProgramIds, Collection<Integer> admissionMethodIds);

    @Query(value = """
    SELECT a.year, atpm.addmission_score, atpm.quota, a.university_id
    FROM admission_training_program_method atpm
    JOIN admission_training_program atp ON atpm.admission_training_program_id = atp.id
    JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
    JOIN admission_method am ON atpm.admission_method_id = am.id
    JOIN admission a ON atp.admission_id = a.id
    WHERE atpsg.subject_group_id = :subjectGroupId
      AND am.method_id = 1
      AND a.university_id = :universityId
      AND atp.major_id IN :majorId
      AND a.year IN (2023, 2024)
    ORDER BY a.year ASC
    """, nativeQuery = true)
    List<Object[]> findAdmissionDataFor2023And2024WithMajor(
            @Param("universityId") int universityId,
            @Param("subjectGroupId") int subjectGroupId,
            @Param("majorId") List<Integer> majorId);

    @Query(value = """
    SELECT a.year, atpm.addmission_score, atpm.quota, a.university_id
    FROM admission_training_program_method atpm
    JOIN admission_training_program atp ON atpm.admission_training_program_id = atp.id
    JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
    JOIN admission_method am ON atpm.admission_method_id = am.id
    JOIN admission a ON atp.admission_id = a.id
    WHERE atpsg.subject_group_id = :subjectGroupId
      AND am.method_id = 1
      AND a.university_id = :universityId
      AND a.year IN (2023, 2024)
    ORDER BY a.year ASC
    """, nativeQuery = true)
    List<Object[]> findAdmissionDataFor2023And2024WithoutMajor(
            @Param("universityId") int universityId,
            @Param("subjectGroupId") int subjectGroupId);


    @Query(value = "SELECT atpm.addmission_score " +
            "FROM admission_training_program_method atpm " +
            "JOIN admission_training_program atp ON atpm.admission_training_program_id = atp.id " +
            "JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id " +
            "JOIN admission a ON atp.admission_id = a.id " +
            "JOIN university_info u ON a.university_id = u.university_id " +
            "JOIN major m ON atp.major_id = m.id " +
            "JOIN subject_group sg ON atpsg.subject_group_id = sg.id " +
            "WHERE a.year = 2023 " +
            "AND u.name = :universityName " +
            "AND m.name = :majorName " +
            "AND sg.name = :subjectGroupName",
            nativeQuery = true)
    Float findScoreFor2023(@Param("universityName") String universityName,
                           @Param("majorName") String majorName,
                           @Param("subjectGroupName") String subjectGroupName);

    @Transactional
    @Modifying
    @Query("delete from AdmissionTrainingProgramMethod a where a.id in ?1")
    int deleteByIdIn(Collection<AdmissionTrainingProgramMethodId> ids);
}



