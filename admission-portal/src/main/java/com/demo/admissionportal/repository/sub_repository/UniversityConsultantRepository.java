package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.UniversityConsultant;
import com.demo.admissionportal.entity.sub_entity.id.UniversityConsultantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversityConsultantRepository extends JpaRepository<UniversityConsultant, UniversityConsultantId> {
    Optional<UniversityConsultant> findByConsultantIdAndUniversityId(Integer consultantId, Integer universityId);

}
