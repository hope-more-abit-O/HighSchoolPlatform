package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    Optional<Report> findById(Integer reportId);
}
