package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.admission.SchoolDirectoryInfoDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import com.demo.admissionportal.dto.response.sub_entity.SubjectResponseDTO2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDirectoryInfoResponse {
    private List<InfoMajorDTO> majors;
    private List<InfoMethodDTO> methods;
    private List<SubjectGroupResponseDTO2> subjectGroups;
    private List<SubjectResponseDTO2> subjects;
    private Page<SchoolDirectoryInfoDTO> page;
}