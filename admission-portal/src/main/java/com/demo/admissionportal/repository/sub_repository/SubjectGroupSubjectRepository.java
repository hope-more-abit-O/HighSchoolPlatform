package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.SubjectGroup;
import com.demo.admissionportal.entity.sub_entity.SubjectGroupSubject;
import com.demo.admissionportal.entity.sub_entity.id.SubjectGroupSubjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * The interface Subject group subject repository.
 */
@Repository
public interface SubjectGroupSubjectRepository extends JpaRepository<SubjectGroupSubject, SubjectGroupSubjectId> {
    /**
     * Exists by subject id and subject group id boolean.
     *
     * @param subjectId      the subject id
     * @param subjectGroupId the subject group id
     * @return the boolean
     */
    boolean existsBySubjectIdAndSubjectGroupId(Integer subjectId, Integer subjectGroupId);

    /**
     * Find by subject group id list.
     *
     * @param subjectGroupId the subject group id
     * @return the list
     */
    List<SubjectGroupSubject> findBySubjectGroupId(Integer subjectGroupId);

    /**
     * Find by subject id list.
     *
     * @param subjectId the subject id
     * @return the list
     */
    List<SubjectGroupSubject> findBySubjectId(Integer subjectId);

    @Query("SELECT DISTINCT sg FROM SubjectGroupSubject sgs JOIN SubjectGroup sg ON sgs.subjectGroupId = sg.id WHERE sgs.subjectId IN :subjectIds")
    List<SubjectGroup> findBySubjectIdIn(@Param("subjectIds") List<Integer> subjectIds);

    @Query("SELECT s.subjectId FROM SubjectGroupSubject s WHERE s.subjectGroupId = :subjectGroupId")
    List<Integer> findSubjectIdsBySubjectGroupId(@Param("subjectGroupId") Integer subjectGroupId);

    List<SubjectGroupSubject> findBySubjectGroupIdIn(Collection<Integer> subjectGroupIds);
}