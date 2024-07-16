package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.SubjectGradeSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectGradeSemesterRepository extends JpaRepository<SubjectGradeSemester, Integer> {
}
