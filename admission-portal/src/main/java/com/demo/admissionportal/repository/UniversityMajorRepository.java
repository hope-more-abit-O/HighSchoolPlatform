package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityMajor;
import com.demo.admissionportal.entity.sub_entity.id.UniversityMajorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityMajorRepository extends JpaRepository<UniversityMajor, UniversityMajorId> {
    List<UniversityMajor> findById_UniversityId(Integer universityId);

    List<UniversityMajor> findById_MajorId(Integer majorId);

    List<UniversityMajor> findById_MajorIdIn(List<Integer> majorIds);

    List<UniversityMajor> findById_UniversityIdAndId_MajorIdIn(Integer universityId, List<Integer> majorIds);
}
