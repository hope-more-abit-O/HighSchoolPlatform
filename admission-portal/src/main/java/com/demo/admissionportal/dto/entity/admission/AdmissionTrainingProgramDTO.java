package com.demo.admissionportal.dto.entity.admission;


import com.demo.admissionportal.dto.entity.major.FullMajorDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.subject.InfoSubjectDTO;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.entity.UniversityTrainingProgram;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  AdmissionTrainingProgramDTO {
    private Integer id;
    private Integer universityTrainingProgramId;
    private Integer admissionId;
    private InfoMajorDTO major;
    private InfoSubjectDTO mainSubjectName;
    private String language;
    private String trainingSpecific;
    private String trainingProgramCode;

    public AdmissionTrainingProgramDTO(AdmissionTrainingProgram admissionTrainingProgram, List<Subject> subjects, List<Major> majors) {
        this.id = admissionTrainingProgram.getId();
        this.admissionId = admissionTrainingProgram.getAdmissionId();
        this.major = majors.stream().filter( (element) -> element.getId().equals(admissionTrainingProgram.getMajorId()))
                .map(InfoMajorDTO::new)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên ngành", Map.of("majorId", admissionTrainingProgram.getMajorId().toString())));
        if (admissionTrainingProgram.getMainSubjectId() != null)
            this.mainSubjectName = subjects.stream().filter( (element) -> element.getId().equals(admissionTrainingProgram.getMainSubjectId()))
                    .map(InfoSubjectDTO::new)
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học", Map.of("mainSubjectId", admissionTrainingProgram.getMainSubjectId().toString())));
        this.language = admissionTrainingProgram.getLanguage();
        this.trainingSpecific = admissionTrainingProgram.getTrainingSpecific();
        this.trainingProgramCode = admissionTrainingProgram.getTrainingProgramCode();
    }

    public AdmissionTrainingProgramDTO(AdmissionTrainingProgram admissionTrainingProgram, List<Subject> subjects, List<Major> majors, List<UniversityTrainingProgram> universityTrainingPrograms) {
        this.id = admissionTrainingProgram.getId();
        this.universityTrainingProgramId = universityTrainingPrograms.stream().filter((element) -> element.compareWithAdmissionTrainingProgram(admissionTrainingProgram)).findFirst().map(UniversityTrainingProgram::getId).orElse(null);
        this.admissionId = admissionTrainingProgram.getAdmissionId();
        this.major = majors.stream().filter( (element) -> element.getId().equals(admissionTrainingProgram.getMajorId()))
                .map(InfoMajorDTO::new)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên ngành", Map.of("majorId", admissionTrainingProgram.getMajorId().toString())));
        if (admissionTrainingProgram.getMainSubjectId() != null)
            this.mainSubjectName = subjects.stream().filter( (element) -> element.getId().equals(admissionTrainingProgram.getMainSubjectId()))
                    .map(InfoSubjectDTO::new)
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học", Map.of("mainSubjectId", admissionTrainingProgram.getMainSubjectId().toString())));
        this.language = admissionTrainingProgram.getLanguage();
        this.trainingSpecific = admissionTrainingProgram.getTrainingSpecific();
        this.trainingProgramCode = admissionTrainingProgram.getTrainingProgramCode();
    }
}