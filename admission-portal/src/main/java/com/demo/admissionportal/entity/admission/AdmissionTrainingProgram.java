package com.demo.admissionportal.entity.admission;

import com.demo.admissionportal.dto.entity.admission.CreateTrainingProgramRequest;
import com.demo.admissionportal.dto.entity.admission.TrainingProgramDTO;
import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admission_training_program")
@AllArgsConstructor
@NoArgsConstructor
public class AdmissionTrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "major_id", nullable = false)
    private Integer majorId;

    @NotNull
    @Column(name = "admission_id", nullable = false)
    private Integer admissionId;

    @Column(name = "main_subject_id")
    private Integer mainSubjectId;

    @Size(max = 255)
    @Column(name = "\"language\"")
    private String language;

    @Size(max = 255)
    @Column(name = "training_specific")
    private String trainingSpecific;

    @Column(name = "quota")
    private Integer quota;


    public AdmissionTrainingProgram(Integer admissionId, CreateTrainingProgramRequest createTrainingProgramRequest) {
        this.admissionId = admissionId;
        this.majorId = createTrainingProgramRequest.getMajorId();
        this.mainSubjectId = createTrainingProgramRequest.getMainSubjectId();
        this.language = createTrainingProgramRequest.getLanguage();
        this.trainingSpecific = createTrainingProgramRequest.getTrainingSpecific();
    }
    public AdmissionTrainingProgram(Integer admissionId, TrainingProgramDTO trainingProgramDTOs) {
        this.admissionId = admissionId;
        this.majorId = trainingProgramDTOs.getMajorId();
        this.mainSubjectId = trainingProgramDTOs.getMainSubjectId();
        this.language = trainingProgramDTOs.getLanguage();
        this.trainingSpecific = trainingProgramDTOs.getTrainingSpecific();
    }

    public AdmissionTrainingProgram(Integer admissionId, CreateAdmissionQuotaRequest request) {
        this.admissionId = admissionId;
        this.majorId = request.getMajorId();
        this.mainSubjectId = request.getMainSubjectId();
        this.language = request.getLanguage();
        this.trainingSpecific = request.getTrainingSpecific();
    }

    @Override
    public String toString() {
        return "AdmissionTrainingProgram{" +
                "id=" + id +
                ", majorId=" + majorId +
                ", admissionId=" + admissionId +
                ", mainSubjectId=" + mainSubjectId +
                ", language='" + language + '\'' +
                ", trainingSpecific='" + trainingSpecific + '\'' +
                ", quota=" + quota +
                '}';
    }
}