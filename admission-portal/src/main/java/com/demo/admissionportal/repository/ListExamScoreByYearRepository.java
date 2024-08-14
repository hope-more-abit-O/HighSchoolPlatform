package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.ListExamScoreByYearStatus;
import com.demo.admissionportal.entity.ListExamScoreByYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListExamScoreByYearRepository extends JpaRepository<ListExamScoreByYear, Integer> {
    ListExamScoreByYear findByYear(int year);
    Page<ListExamScoreByYear> findAll(Pageable pageable);

    List<ListExamScoreByYear> findAllByStatus(ListExamScoreByYearStatus status);

    ListExamScoreByYear findByYear(Integer year);
}
