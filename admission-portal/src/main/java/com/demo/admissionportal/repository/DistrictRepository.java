package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.address.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    @Query(value = "SELECT d.id, d.name " +
            "FROM district d " +
            "INNER JOIN province_district pd ON d.id = pd.district_id " +
            "WHERE pd.province_id = :id", nativeQuery = true)
    public List<District> getDistrictsByProvinceId(Integer id);
}
