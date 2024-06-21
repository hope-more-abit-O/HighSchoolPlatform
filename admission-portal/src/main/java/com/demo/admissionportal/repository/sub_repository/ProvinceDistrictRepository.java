package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.ProvinceDistrict;
import com.demo.admissionportal.entity.sub_entity.id.ProvinceDistrictId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceDistrictRepository extends JpaRepository<ProvinceDistrict, ProvinceDistrictId> {
}
