package com.demo.admissionportal.entity.admission;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admission_method")
public class AdmissionMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "method_id", nullable = false)
    private Integer method;

    @NotNull
    @Column(name = "admission_id", nullable = false)
    private Integer admission;

    @Column(name = "quota")
    private Integer quota;

}