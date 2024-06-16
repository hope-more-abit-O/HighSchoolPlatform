package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.UniversityConsultant;
import com.demo.admissionportal.entity.sub_entity.id.UniversityConsultantId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityConsultantRepository extends JpaRepository<UniversityConsultant, UniversityConsultantId> {

}
