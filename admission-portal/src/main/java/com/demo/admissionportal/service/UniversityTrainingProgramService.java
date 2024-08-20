package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.university_training_program.GetFullUniversityTrainingProgramResponse;
import com.demo.admissionportal.dto.response.university_training_program.GetInfoUniversityTrainingProgramResponse;

public interface UniversityTrainingProgramService {
    GetFullUniversityTrainingProgramResponse getUniversityTrainingPrograms(Integer universityId);
    GetInfoUniversityTrainingProgramResponse getInfoUniversityTrainingPrograms(Integer universityId);
}
