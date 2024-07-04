package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "thumnail")
    private String thumbnail;

    @Column(name = "quota")
    private String quota;

    @Column(name = "view")
    private Integer view;

    @Column(name = "like")
    private Integer like;

    @NotNull
    @ColumnDefault("'ACTIVE'")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
}
