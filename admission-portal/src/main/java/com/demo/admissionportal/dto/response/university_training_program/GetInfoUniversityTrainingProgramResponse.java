package com.demo.admissionportal.dto.response.university_training_program;

import com.demo.admissionportal.dto.entity.university_training_program.InfoUniversityTrainingProgramDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInfoUniversityTrainingProgramResponse {
    private List<InfoUniversityTrainingProgramDTO> universityTrainingPrograms;
}
