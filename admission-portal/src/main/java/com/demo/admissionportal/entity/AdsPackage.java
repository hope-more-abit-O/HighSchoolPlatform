package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.PackageStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ads_package")
public class AdsPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Nationalized
    @Column(name = "name")
    private String name;

    @NotNull
    @Nationalized
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "image")
    private String image;

    @NotNull
    @Column(name = "view_boost_value")
    private Integer viewBoostValue;

    @NotNull
    @Column(name = "price")
    private Double price;

    @NotNull
    @Column(name = "create_by")
    private Integer createBy;

    @NotNull
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PackageStatus status;
}