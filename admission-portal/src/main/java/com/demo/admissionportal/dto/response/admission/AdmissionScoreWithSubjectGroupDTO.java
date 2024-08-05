package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionScoreWithSubjectGroupDTO {
    private InfoMajorDTO major;
    private List<SubjectGroupResponseDTO2> subjectGroups;
    private Float admissionScore;
}
