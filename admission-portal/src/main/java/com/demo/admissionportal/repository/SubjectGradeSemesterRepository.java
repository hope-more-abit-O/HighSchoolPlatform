package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.SemesterType;
import com.demo.admissionportal.entity.SubjectGradeSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectGradeSemesterRepository extends JpaRepository<SubjectGradeSemester, Integer> {
    Optional<SubjectGradeSemester> findBySubjectIdAndGradeAndSemester(Integer subjectId, String name, SemesterType semester);
}
