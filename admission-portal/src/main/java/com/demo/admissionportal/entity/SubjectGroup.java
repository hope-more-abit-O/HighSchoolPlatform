package com.demo.admissionportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * The type Subject group.
 */
@Data
@Entity
@Table(name = "subject_group")
public class SubjectGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 3)
    @NotNull
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "status")
    private String status;
}
