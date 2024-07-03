package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityInfoRepository extends JpaRepository<UniversityInfo, Integer> {
}
