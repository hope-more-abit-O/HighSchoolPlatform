package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.StudentReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Student report repository.
 */
@Repository
public interface StudentReportRepository extends JpaRepository<StudentReport, Integer> {
    @Query(value = "SELECT * FROM student_report WHERE create_by = :userId AND status != 'INACTIVE' AND (:name IS NULL OR name LIKE %:#{#name}% ) ORDER BY create_time DESC", nativeQuery = true)
    Page<StudentReport> findAll(Integer userId, String name, Pageable pageable);

    @Query(value = "SELECT * FROM student_report WHERE id = :id AND status != 'INACTIVE'", nativeQuery = true)
    Optional<StudentReport> findById(Integer id);
}
