package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.address.District;
import com.demo.admissionportal.entity.address.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    @Query(value = "SELECT w.id, w.name " +
            "FROM ward w " +
            "INNER JOIN district_ward dw ON w.id = dw.ward_id " +
            "WHERE dw.district_id = :id", nativeQuery = true)
    public List<Ward> getWardsByDistrictId(Integer id);
}
