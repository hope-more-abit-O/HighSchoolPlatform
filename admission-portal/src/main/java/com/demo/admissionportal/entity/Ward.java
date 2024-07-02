package com.demo.admissionportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Nationalized;

/**
 * The type Ward.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ward")
public class Ward {
    @Id
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Nationalized
    @Column(name = "name")
    private String name;

}