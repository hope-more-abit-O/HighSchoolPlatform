package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.DistrictWard;
import com.demo.admissionportal.entity.sub_entity.id.DistrictWardId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictWardRepository extends JpaRepository<DistrictWard, DistrictWardId> {
}
