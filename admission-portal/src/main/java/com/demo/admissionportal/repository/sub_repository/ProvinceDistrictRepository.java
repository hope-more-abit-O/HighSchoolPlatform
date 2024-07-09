package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.ProvinceDistrict;
import com.demo.admissionportal.entity.sub_entity.id.ProvinceDistrictId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Province district repository.
 */
public interface ProvinceDistrictRepository extends JpaRepository<ProvinceDistrict, ProvinceDistrictId> {

    /**
     * Find by province id list.
     *
     * @param provinceId the province id
     * @return the list
     */
    List<ProvinceDistrict> findByProvinceId(Integer provinceId);
}
