package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.SemesterType;
import com.demo.admissionportal.entity.SubjectGradeSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Subject grade semester repository.
 */
@Repository
public interface SubjectGradeSemesterRepository extends JpaRepository<SubjectGradeSemester, Integer> {
    /**
     * Find by subject id and grade and semester optional.
     *
     * @param subjectId the subject id
     * @param grade     the grade
     * @param semester  the semester
     * @return the optional
     */
    List<SubjectGradeSemester> findBySubjectIdAndGradeAndSemester(Integer subjectId, Integer grade, SemesterType semester);
}
