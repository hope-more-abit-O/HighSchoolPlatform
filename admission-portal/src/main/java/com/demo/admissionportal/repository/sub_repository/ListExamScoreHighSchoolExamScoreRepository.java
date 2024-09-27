package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.constants.HighschoolExamScoreStatus;
import com.demo.admissionportal.entity.sub_entity.ListExamScoreHighSchoolExamScore;
import com.demo.admissionportal.entity.sub_entity.id.ListExamScoreHighSchoolExamScoreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ListExamScoreHighSchoolExamScoreRepository extends JpaRepository<ListExamScoreHighSchoolExamScore, ListExamScoreHighSchoolExamScoreId> {
    List<ListExamScoreHighSchoolExamScore> findAllByListExamScoreByYearId(Integer listExamScoreByYearId);

    @Modifying
    @Transactional
    @Query("UPDATE ListExamScoreHighSchoolExamScore l SET l.status = :status WHERE l.listExamScoreByYearId = :listExamScoreByYearId")
    int updateStatusByListExamScoreByYearId(@Param("status") HighschoolExamScoreStatus status, @Param("listExamScoreByYearId") Integer listExamScoreByYearId);

    @Modifying
    @Transactional
    @Query("UPDATE ListExamScoreHighSchoolExamScore l SET l.status = :inactiveStatus WHERE l.listExamScoreByYearId != :listExamScoreByYearId")
    int deactivateOtherHighSchoolExamScores(@Param("listExamScoreByYearId") Integer listExamScoreByYearId,
                                            @Param("inactiveStatus") HighschoolExamScoreStatus inactiveStatus);

}
