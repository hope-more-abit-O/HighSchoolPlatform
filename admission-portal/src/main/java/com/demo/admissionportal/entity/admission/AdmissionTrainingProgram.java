package com.demo.admissionportal.entity.admission;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admission_training_program")
public class AdmissionTrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "major_id", nullable = false)
    private Integer major;

    @NotNull
    @Column(name = "admission_id", nullable = false)
    private Integer admission;

    @NotNull
    @Column(name = "main_subject_id", nullable = false)
    private Integer mainSubject;

    @Size(max = 255)
    @Column(name = "\"language\"")
    private String language;

    @Size(max = 255)
    @Column(name = "training_specific")
    private String trainingSpecific;

    @Column(name = "quota")
    private Integer quota;

}