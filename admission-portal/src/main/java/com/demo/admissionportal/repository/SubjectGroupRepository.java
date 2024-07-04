package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.SubjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Subject group repository.
 */
@Repository
public interface SubjectGroupRepository extends JpaRepository<SubjectGroup, Integer> {
}