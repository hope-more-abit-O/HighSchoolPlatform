package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.AdsPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Package repository.
 */
@Repository
public interface PackageRepository extends JpaRepository<AdsPackage, Integer> {
}
