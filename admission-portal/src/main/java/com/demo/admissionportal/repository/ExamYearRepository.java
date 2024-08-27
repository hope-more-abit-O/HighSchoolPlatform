package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.ExamYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamYearRepository extends JpaRepository<ExamYear, Integer> {
    ExamYear findByYear(int year);
}