package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardRepository extends JpaRepository<Ward, Integer> {
}