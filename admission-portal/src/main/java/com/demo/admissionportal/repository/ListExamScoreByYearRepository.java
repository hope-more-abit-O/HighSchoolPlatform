package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.ListExamScoreByYearStatus;
import com.demo.admissionportal.entity.ListExamScoreByYear;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListExamScoreByYearRepository extends JpaRepository<ListExamScoreByYear, Integer> {
    ListExamScoreByYear findByYear(int year);
    Page<ListExamScoreByYear> findAll(Pageable pageable);

    List<ListExamScoreByYear> findAllByStatus(ListExamScoreByYearStatus status);

    ListExamScoreByYear findByYear(Integer year);

    @Modifying
    @Transactional
    @Query("UPDATE ListExamScoreByYear l SET l.status = :inactiveStatus WHERE l.id != :listExamScoreByYearId")
    int deactivateOtherExamScoreByYears(@Param("listExamScoreByYearId") Integer listExamScoreByYearId,
                                        @Param("inactiveStatus") ListExamScoreByYearStatus inactiveStatus);

}
