package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import com.demo.admissionportal.entity.SubjectGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionTrainingProgramScoreDTO {
    private Integer year;
    private List<SubjectGroupResponseDTO2> subjectGroups;
    private Float score;
}
