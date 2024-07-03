package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {
}