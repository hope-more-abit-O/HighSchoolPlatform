package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.StudentReportHighSchoolScore;
import com.demo.admissionportal.entity.sub_entity.id.StudentReportHighSchoolScoreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentReportHighSchoolScoreRepository extends JpaRepository<StudentReportHighSchoolScore, StudentReportHighSchoolScoreId> {

    List<StudentReportHighSchoolScore> findById_StudentReportId(Integer studentReportId);

    List<StudentReportHighSchoolScore> findById_StudentReportIdAndId_SubjectIdIn(Integer studentReportId, List<Integer> subjectIds);

    @Transactional
    @Modifying
    @Query("delete from StudentReportHighSchoolScore s where s.id.studentReportId = ?1")
    void deleteById_StudentReportId(Integer studentReportId);
}
