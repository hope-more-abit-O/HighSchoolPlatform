package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityCampus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface University campus repository.
 */
@Repository
public interface UniversityCampusRepository extends JpaRepository<UniversityCampus, Integer> {
    /**
     * Find university campus by id university campus.
     *
     * @param id the id
     * @return the university campus
     */
    UniversityCampus findUniversityCampusByUniversityId(Integer id);
}
