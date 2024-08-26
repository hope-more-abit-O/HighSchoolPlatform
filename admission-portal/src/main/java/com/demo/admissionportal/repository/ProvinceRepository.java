package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.Region;
import com.demo.admissionportal.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

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

    List<Province> findByRegion(Region region);

    List<Province> findByRegionIn(Collection<Region> regions);
}
