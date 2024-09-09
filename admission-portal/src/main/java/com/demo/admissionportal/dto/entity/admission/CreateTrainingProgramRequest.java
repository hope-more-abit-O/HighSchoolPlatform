package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainingProgramRequest {
    @NotNull(message = "Mã ngành không được để trống")
    private Integer majorId;

    private Integer mainSubjectId;

    private String language;

    private String trainingSpecific;

    public boolean isEqual(AdmissionTrainingProgram admissionTrainingProgram){
        return this.majorId.equals(admissionTrainingProgram.getMajorId()) &&
                this.mainSubjectId.equals(admissionTrainingProgram.getMainSubjectId()) &&
                this.language.equals(admissionTrainingProgram.getLanguage()) &&
                this.trainingSpecific.equals(admissionTrainingProgram.getTrainingSpecific());
    }

    @Override
    public String toString() {
        return "CreateTrainingProgramRequest{" +
                "majorId=" + majorId +
                ", mainSubjectId=" + (mainSubjectId != null ? mainSubjectId : "null") +
                ", language='" + (language != null ? language : "null") + '\'' +
                ", trainingSpecific='" + (trainingSpecific != null ? trainingSpecific : "null") + '\'' +
                '}';
    }
}
