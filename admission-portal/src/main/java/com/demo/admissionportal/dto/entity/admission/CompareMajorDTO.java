package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.admission.Admission;
import com.demo.admissionportal.entity.admission.AdmissionMethod;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompareMajorDTO {
    private InfoMajorDTO major;
    private Integer admissionTrainingProgramId;
    private Integer universityTrainingProgramId;
    private Integer admissionId;
    private String trainingSpecific;
    private String language;
    private String trainingProgramCode;
    private List<CompareMajorMethodDTO> methodAndScores;


    public CompareMajorDTO(AdmissionTrainingProgram admissionTrainingProgram, InfoMajorDTO major, Admission admission, List<UniversityTrainingProgram> universityTrainingPrograms) {
        this.major = major;
        this.admissionTrainingProgramId = admissionTrainingProgram.getId();
        UniversityTrainingProgram universityTrainingProgram = universityTrainingPrograms
                .stream()
                .filter((ele) -> ele.compareWithAdmissionTrainingProgram(admissionTrainingProgram))
                .findFirst()
                .orElse(null);
        if (universityTrainingProgram != null)
            this.universityTrainingProgramId = universityTrainingProgram.getId();
        else
            this.universityTrainingProgramId = null;
        this.admissionId = admission.getId();
        this.trainingSpecific = admissionTrainingProgram.getTrainingSpecific();
        this.language = admissionTrainingProgram.getLanguage();
        this.trainingProgramCode = admissionTrainingProgram.getTrainingProgramCode();
    }

    public CompareMajorDTO(AdmissionTrainingProgram admissionTrainingProgram, InfoMajorDTO major) {
        this.major = major;
        this.trainingSpecific = admissionTrainingProgram.getTrainingSpecific();
        this.language = admissionTrainingProgram.getLanguage();
        this.trainingProgramCode = admissionTrainingProgram.getTrainingProgramCode();
    }
}