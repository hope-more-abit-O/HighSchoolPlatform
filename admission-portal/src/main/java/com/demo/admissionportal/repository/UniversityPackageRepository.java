package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityPackage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface University package repository.
 */
public interface UniversityPackageRepository extends JpaRepository<UniversityPackage, Integer> {
}
