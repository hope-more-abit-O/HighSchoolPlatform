package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface District repository.
 */
@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    /**
     * Find district by id district.
     *
     * @param id the id
     * @return the district
     */
    District findDistrictById(Integer id);
}
