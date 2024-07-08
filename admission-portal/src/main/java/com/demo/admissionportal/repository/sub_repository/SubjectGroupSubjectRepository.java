package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.SubjectGroupSubject;
import com.demo.admissionportal.entity.sub_entity.id.SubjectGroupSubjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectGroupSubjectRepository extends JpaRepository<SubjectGroupSubject, SubjectGroupSubjectId> {
    boolean existsBySubjectIdAndSubjectGroupId(Integer subjectId, Integer subjectGroupId);
    List<SubjectGroupSubject> findBySubjectGroupId(Integer subjectGroupId);
    List<SubjectGroupSubject> findBySubjectId(Integer subjectId);
}