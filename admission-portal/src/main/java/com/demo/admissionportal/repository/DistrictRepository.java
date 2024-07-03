package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Integer> {
}