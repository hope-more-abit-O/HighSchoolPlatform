package com.demo.admissionportal.dto.entity.university_training_program;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoUniversityTrainingProgramDTO {
    private Integer id;
    private Integer universityId;
    private InfoMajorDTO major;
    private String trainingSpecific;
    private String language;
    private String status;
}
