package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.SubjectGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Subject group repository.
 */
@Repository
public interface SubjectGroupRepository extends JpaRepository<SubjectGroup, Integer> {
    /**
     * Find by name subject group.
     *
     * @param name the name
     * @return the subject group
     */
    SubjectGroup findByName(String name);

    /**
     * Find all page.
     *
     * @param groupName    the group name
     * @param subjectName  the subject name
     * @param statusString the status string
     * @param pageable     the pageable
     * @return the page
     */
    @Query(value = "SELECT DISTINCT sg.id, sg.name, sg.status, sg.create_by, sg.update_by, sg.create_time, sg.update_time " +
            "FROM subject_group sg " +
            "LEFT JOIN subject_group_subject sgs ON sg.id = sgs.subject_group_id " +
            "LEFT JOIN subject s ON sgs.subject_id = s.id " +
            "WHERE (:groupName IS NULL OR sg.name LIKE %:groupName%) " +
            "AND (:subjectName IS NULL OR s.name LIKE %:subjectName%) " +
            "AND (:statusString IS NULL OR sg.status = :statusString) " +
            "ORDER BY sg.update_time DESC",
            countQuery = "SELECT COUNT(DISTINCT sg.id) " +
                    "FROM subject_group sg " +
                    "LEFT JOIN subject_group_subject sgs ON sg.id = sgs.subject_group_id " +
                    "LEFT JOIN subject s ON sgs.subject_id = s.id " +
                    "WHERE (:groupName IS NULL OR sg.name LIKE %:groupName%) " +
                    "AND (:subjectName IS NULL OR s.name LIKE %:subjectName%) " +
                    "AND (:statusString IS NULL OR sg.status = :statusString)",
            nativeQuery = true)
    Page<SubjectGroup> findAll(@Param("groupName") String groupName,
                               @Param("subjectName") String subjectName,
                               @Param("statusString") String statusString,
                               Pageable pageable);

    @Query(value = """
    SELECT sg.*
    FROM [subject_group] sg
    RIGHT JOIN [admission_training_program_subject_group] atpsg ON sg.id = atpsg.subject_group_id
    WHERE (atpsg.admission_training_program_id IN (:admissionTrainingProgramIds))""", nativeQuery = true)
    List<SubjectGroup> findByAdmissionTrainingProgramId(@Param("admissionTrainingProgramIds") List<Integer> admissionTrainingProgramIds);

    @Query("SELECT sg FROM SubjectGroup sg WHERE sg.name LIKE :group%")
    List<SubjectGroup> findByNameGroup(@Param("group") String group);

//    @Query(value = """
//select distinct sg.*
//from subject_group sg
//inner join subject_group_subject sgs on sgs.subject_group_id = sg.id
//inner join admission_training_program_subject_group atpsg on sg.id = atpsg.subject_group_id
//where subject_id in (:subjectIds)
//and atpsg.admission_training_program_id in (:admissionTrainingProgramIds)
//""", nativeQuery = true)
//    List<SubjectGroup> findByAdmissionTrainingProgramIdAndSubjectId(@Param("subjectIds") List<Integer> subjectIds, @Param("admissionTrainingProgramIds") List<Integer> admissionTrainingProgramIds);

    @Query("SELECT sg.id FROM SubjectGroup sg WHERE sg.name = :examGroup")
    Integer findSubjectGroupIdByName(@Param("examGroup") String examGroup);

    @Query(value = """
select distinct sg.*
from admission_training_program atp
inner join dbo.admission a on a.id = atp.admission_id
inner join dbo.admission_training_program_subject_group atpsg on atp.id = atpsg.admission_training_program_id
inner join subject_group sg on atpsg.subject_group_id = sg.id
where a.status = 'ACTIVE' and a.year = :year
and atp.major_id = :majorId
and a.university_id = :universityId
""", nativeQuery = true)
    List<SubjectGroup> findByMajorIdAndUniversityIdAndYear(Integer majorId, Integer universityId, Integer year);

    SubjectGroup findSubjectGroupById(Integer subjectGroupId);
}
