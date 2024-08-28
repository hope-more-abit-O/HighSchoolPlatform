package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.university_campus.CampusProvinceDTO;
import com.demo.admissionportal.dto.entity.university_training_program.InfoUniversityTrainingProgramDTO;
import com.demo.admissionportal.entity.UniversityTrainingProgram;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDirectoryDetail {
    private Integer id;
    private Integer admissionId;
    private String avatar;
    private String name;
    private String code;
    private String type;
    private String description;
    private String coverImage;
    private Integer universityTrainingProgramCount;
    private Integer campusCount;
    private Integer totalQuota;
    private List<CampusProvinceDTO> campus;
    private List<InfoUniversityTrainingProgramDTO> universityTrainingPrograms;
}
