package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface University info repository.
 */
@Repository
public interface UniversityInfoRepository extends JpaRepository<UniversityInfo, Integer> {
    /**
     * Find university info by id university info.
     *
     * @param id the id
     * @return the university info
     */
    UniversityInfo findUniversityInfoById(Integer id);
}
