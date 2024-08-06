package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface University package repository.
 */
public interface UniversityPackageRepository extends JpaRepository<UniversityPackage, Integer> {
    /**
     * Find university package by transaction id university package.
     *
     * @param transactionId the transaction id
     * @return the university package
     */
    @Query(value = "SELECT * " +
            "FROM [university_package] up " +
            "WHERE up.university_transaction_id = :transactionId", nativeQuery = true)
    UniversityPackage findUniversityPackageByTransactionId(Integer transactionId);
}
