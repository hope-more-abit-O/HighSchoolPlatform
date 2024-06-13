package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.StaffUniversity;
import com.demo.admissionportal.entity.sub_entity.id.StaffUniversityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffUniversityRepository extends JpaRepository<StaffUniversity, StaffUniversityId> {
    Optional<StaffUniversity> findByStaffIdAndUniversityId(Integer staffId, Integer universityId);
}
