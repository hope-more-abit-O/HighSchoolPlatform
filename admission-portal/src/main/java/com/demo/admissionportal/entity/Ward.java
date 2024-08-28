package com.demo.admissionportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;

/**
 * The type Ward.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ward")
public class Ward implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Nationalized
    @Column(name = "name")
    private String name;

    public Ward(Integer id) {
        this.id = id;
    }
}