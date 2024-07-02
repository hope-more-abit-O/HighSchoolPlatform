package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Province repository.
 */
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    /**
     * Find province by id province.
     *
     * @param id the id
     * @return the province
     */
    Province findProvinceById(Integer id);
}
