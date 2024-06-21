package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.address.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
}
