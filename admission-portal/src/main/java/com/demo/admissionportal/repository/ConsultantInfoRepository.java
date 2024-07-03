package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.ConsultantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultantInfoRepository extends JpaRepository<ConsultantInfo, Integer> {
    Optional<ConsultantInfo> findFirstByPhone(String phone);
}
