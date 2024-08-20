package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityTrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UniversityTrainingProgramRepository extends JpaRepository<UniversityTrainingProgram, Integer> {
    List<UniversityTrainingProgram> findByUniversityId(Integer universityId);

    List<UniversityTrainingProgram> findByIdInAndUniversityId(Collection<Integer> ids, Integer universityId);
}
