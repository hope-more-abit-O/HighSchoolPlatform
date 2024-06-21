package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.address.AddressDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressDetailRepository extends JpaRepository<AddressDetail, Integer> {
    Optional<AddressDetail> findByProvinceIdAndDistrictIdAndWardId(Integer provinceId, Integer districtId, Integer wardId);
}
