package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.StaffInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<StaffInfo, Integer> {
}
