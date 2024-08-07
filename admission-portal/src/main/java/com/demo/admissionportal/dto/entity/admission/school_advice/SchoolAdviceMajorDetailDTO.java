package com.demo.admissionportal.dto.entity.admission.school_advice;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolAdviceMajorDetailDTO {
    private InfoMajorDTO major;
    private List<SubjectGroupResponseDTO2> subject1;
    private List<SubjectGroupResponseDTO2> subject2;
    private Float score1;
    private Float score2;
}
