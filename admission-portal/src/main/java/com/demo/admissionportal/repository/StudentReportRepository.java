package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.StudentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentReportRepository extends JpaRepository<StudentReport, Integer> {
    StudentReport findByName(String name);
}
