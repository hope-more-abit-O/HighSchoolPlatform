package com.demo.admissionportal.dto.entity.admission;


import com.demo.admissionportal.dto.entity.major.FullMajorDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.subject.InfoSubjectDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdmissionTrainingProgramDTO {
    private Integer id;
    private Integer admissionId;
    private InfoMajorDTO major;
    private InfoSubjectDTO mainSubjectName;
    private String language;
    private String trainingSpecific;
}
