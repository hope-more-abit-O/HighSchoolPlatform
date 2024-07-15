package com.demo.admissionportal.entity.admission;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admission_method")
public class AdmissionMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "method_id", nullable = false)
    private Integer methodId;

    @NotNull
    @Column(name = "admission_id", nullable = false)
    private Integer admissionId;

    @Column(name = "quota")
    private Integer quota;

    public AdmissionMethod(Integer admissionId, Integer methodId) {
        this.methodId = methodId;
        this.admissionId = admissionId;
    }

}