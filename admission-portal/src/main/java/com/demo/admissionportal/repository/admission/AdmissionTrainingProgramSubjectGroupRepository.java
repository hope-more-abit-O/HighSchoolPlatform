package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramSubjectGroup;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramSubjectGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AdmissionTrainingProgramSubjectGroupRepository extends JpaRepository<AdmissionTrainingProgramSubjectGroup, AdmissionTrainingProgramSubjectGroupId> {
    List<AdmissionTrainingProgramSubjectGroup> findById_AdmissionTrainingProgramIdIn(Collection<Integer> admissionTrainingProgramIds);

    List<AdmissionTrainingProgramSubjectGroup> findById_AdmissionTrainingProgramIdInAndId_SubjectGroupIdIn(List<Integer> admissionTrainingProgramIds, List<Integer> subjectGroupIds);

    @Query(value = """
        SELECT atpsg.*
        FROM admission_training_program_subject_group atpsg
        JOIN admission_training_program atp ON atpsg.admission_training_program_id = atp.id
        JOIN admission a ON atp.admission_id = a.id
        WHERE a.university_id = :universityId
        AND atpsg.subject_group_id = :subjectGroupId
        """, nativeQuery = true)
    List<AdmissionTrainingProgramSubjectGroup> findByUniversityIdAndSubjectGroupId(@Param("universityId") int universityId,
                                                                                   @Param("subjectGroupId") int subjectGroupId);
}
