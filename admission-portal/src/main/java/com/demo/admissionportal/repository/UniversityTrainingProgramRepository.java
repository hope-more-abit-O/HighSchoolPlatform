package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.UniversityTrainingProgramStatus;
import com.demo.admissionportal.entity.UniversityTrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UniversityTrainingProgramRepository extends JpaRepository<UniversityTrainingProgram, Integer> {
    List<UniversityTrainingProgram> findByUniversityId(Integer universityId);

    List<UniversityTrainingProgram> findByIdInAndUniversityId(Collection<Integer> ids, Integer universityId);

    List<UniversityTrainingProgram> findByUniversityIdAndStatus(Integer universityId, UniversityTrainingProgramStatus status);

    List<UniversityTrainingProgram> findByUniversityIdInAndStatus(List<Integer> universityIds, UniversityTrainingProgramStatus universityTrainingProgramStatus);

    List<UniversityTrainingProgram> findByUniversityIdIn(Collection<Integer> universityIds);

    List<UniversityTrainingProgram> findByUniversityIdInAndStatusAndMajorId(Collection<Integer> universityIds, UniversityTrainingProgramStatus status, Integer majorId);

    List<UniversityTrainingProgram> findByMajorId(Integer majorId);

    List<UniversityTrainingProgram> findByUniversityIdInAndStatusAndMajorIdIn(List<Integer> universityIds, UniversityTrainingProgramStatus universityTrainingProgramStatus, List<Integer> majorIds);

    List<UniversityTrainingProgram> findByStatus(UniversityTrainingProgramStatus status);
}