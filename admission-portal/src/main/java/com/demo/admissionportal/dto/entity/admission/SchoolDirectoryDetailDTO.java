package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.SubjectDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.entity.SubjectGroup;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDirectoryDetailDTO {
    private InfoMajorDTO major;
    private SubjectDTO mainSubject;
    private String language;
    private String trainingSpecific;
    private InfoMethodDTO method;
    private Integer quota;
    private Float score;
    private List<SubjectGroupResponseDTO2> subjects;


    public SchoolDirectoryDetailDTO(AdmissionTrainingProgramMethod admissionTrainingProgramMethod, AdmissionTrainingProgram admissionTrainingProgram, Method method, Major major, List<SubjectGroup> subjectGroups1, Subject subject) {
        this.major = new InfoMajorDTO(major);
        this.mainSubject = subject == null ? null : new SubjectDTO(subject);
        this.language = admissionTrainingProgram.getLanguage();
        this.trainingSpecific = admissionTrainingProgram.getTrainingSpecific();
        this.method = new InfoMethodDTO(method);
        this.quota = admissionTrainingProgramMethod.getQuota();
        this.score = admissionTrainingProgramMethod.getAdmissionScore();
        this.subjects = subjectGroups1.stream().map(SubjectGroupResponseDTO2::new).toList();
    }
}
