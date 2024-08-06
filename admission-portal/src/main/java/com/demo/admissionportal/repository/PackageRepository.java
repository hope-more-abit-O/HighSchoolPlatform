package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.AdsPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The interface Package repository.
 */
@Repository
public interface PackageRepository extends JpaRepository<AdsPackage, Integer> {
    /**
     * Find by name ads package.
     *
     * @param packageName the package name
     * @return the ads package
     */
    @Query(value = "SELECT * " +
            "FROM [ads_package] ad " +
            "WHERE ad.name = :packageName", nativeQuery = true)
    AdsPackage findByName(String packageName);

    /**
     * Find package by id ads package.
     *
     * @param id the id
     * @return the ads package
     */
    AdsPackage findPackageById(Integer id);
}
