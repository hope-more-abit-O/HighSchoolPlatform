package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.sub_entity.StudentReportMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentReportMarkRepository extends JpaRepository<StudentReportMark, Integer> {
}
