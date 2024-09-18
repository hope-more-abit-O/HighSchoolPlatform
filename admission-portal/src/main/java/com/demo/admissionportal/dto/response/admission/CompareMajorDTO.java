package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.UniversityTrainingProgram;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.admission.Admission;
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
    private Float score;


    public CompareMajorDTO(AdmissionTrainingProgram admissionTrainingProgram, AdmissionTrainingProgramMethod admissionTrainingProgramMethod, InfoMajorDTO major, Admission admission, List<UniversityTrainingProgram> universityTrainingPrograms) {
        this.major = major;
        this.admissionTrainingProgramId = admissionTrainingProgram.getId();
        this.universityTrainingProgramId = universityTrainingPrograms
                .stream()
                .filter((ele) -> ele.compareWithAdmissionTrainingProgram(admissionTrainingProgram))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy university training program"))
                .getId();
        this.admissionId = admission.getId();
        this.trainingSpecific = admissionTrainingProgram.getTrainingSpecific();
        this.language = admissionTrainingProgram.getLanguage();
        this.score = admissionTrainingProgramMethod.getAdmissionScore();
    }

    public CompareMajorDTO(AdmissionTrainingProgram admissionTrainingProgram, AdmissionTrainingProgramMethod admissionTrainingProgramMethod, InfoMajorDTO major) {
        this.major = major;
        this.trainingSpecific = admissionTrainingProgram.getTrainingSpecific();
        this.language = admissionTrainingProgram.getLanguage();
        this.score = admissionTrainingProgramMethod.getAdmissionScore();
    }

}
