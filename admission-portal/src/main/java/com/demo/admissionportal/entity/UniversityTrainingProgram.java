package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.UniversityTrainingProgramStatus;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "university_training_program")
public class UniversityTrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "university_id", nullable = false)
    private Integer universityId;

    @Column(name = "major_id", nullable = false)
    private Integer majorId;

    @Column(name = "training_specific")
    private String trainingSpecific;

    @Column(name = "language")
    private String language;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UniversityTrainingProgramStatus status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    public void updateStatus(UniversityTrainingProgramStatus status, Integer updateBy){
        this.status = status;
        this.updateBy = updateBy;
        this.updateTime = new Date();
    }

    public UniversityTrainingProgram(AdmissionTrainingProgram admissionTrainingProgram, Integer universityId, Integer createBy) {
        this.universityId = universityId;
        this.majorId = admissionTrainingProgram.getMajorId();
        this.status = UniversityTrainingProgramStatus.ACTIVE;
        this.createTime = new Date();
        this.createBy = createBy;
    }

    public boolean compareWithAdmissionTrainingProgram(AdmissionTrainingProgram admissionTrainingProgram){
        return this.majorId.equals(admissionTrainingProgram.getMajorId())
                && this.trainingSpecific.equals(admissionTrainingProgram.getTrainingSpecific())
                && this.language.equals(admissionTrainingProgram.getLanguage());
    }
}
