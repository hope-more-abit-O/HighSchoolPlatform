package com.demo.admissionportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

/**
 * The type Province.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "province")
public class Province {
    @Id
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Nationalized
    @Column(name = "name")
    private String name;

}