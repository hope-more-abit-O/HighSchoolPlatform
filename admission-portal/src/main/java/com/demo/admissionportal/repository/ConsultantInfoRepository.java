package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.ConsultantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultantInfoRepository extends JpaRepository<ConsultantInfo, Integer> {
}
