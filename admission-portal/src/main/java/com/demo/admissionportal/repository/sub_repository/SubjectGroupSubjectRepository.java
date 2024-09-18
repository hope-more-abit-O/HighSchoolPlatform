package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.SubjectGroupSubject;
import com.demo.admissionportal.entity.sub_entity.id.SubjectGroupSubjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}