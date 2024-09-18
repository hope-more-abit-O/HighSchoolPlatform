package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.dto.entity.university.UniversityInfoResponseDTO;
import com.demo.admissionportal.dto.response.university_training_program.GetFullUniversityTrainingProgramResponse;
import com.demo.admissionportal.dto.response.university_training_program.GetInfoUniversityTrainingProgramResponse;

import java.util.List;

public interface UniversityTrainingProgramService {
    GetFullUniversityTrainingProgramResponse getUniversityTrainingPrograms(Integer universityId);
    GetInfoUniversityTrainingProgramResponse getInfoUniversityTrainingPrograms(Integer universityId);
    GetFullUniversityTrainingProgramResponse getFullUniversityTrainingPrograms(Integer universityId);

    List<UniversityInfoResponseDTO> getUniversitiesHaveMajor(Integer majorId);
}
