package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.sub_entity.StudentReportMark;
import com.demo.admissionportal.entity.sub_entity.id.StudentReportMarkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * The interface Student report mark repository.
 */
@Repository
public interface StudentReportMarkRepository extends JpaRepository<StudentReportMark, StudentReportMarkId> {
    /**
     * Find by student report id list.
     *
     * @param studentReportId the student report id
     * @return the list
     */
    List<StudentReportMark> findByStudentReportId(Integer studentReportId);

    List<StudentReportMark> findByStudentReportIdAndSubjectGradeSemesterIdIn(Integer studentReportId, Collection<Integer> subjectGradeSemesterIds);
}
