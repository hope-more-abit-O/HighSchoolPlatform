package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.PackageStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Nationalized
    @Column(name = "name")
    private String name;

    @NotBlank
    @Nationalized
    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "image")
    private String image;

    @NotBlank
    @Column(name = "view_boost_value")
    private Integer viewBoostValue;

    @NotBlank
    @Column(name = "price")
    private int price;

    @NotBlank
    @Column(name = "create_by")
    private Integer createBy;

    @NotBlank
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    @NotBlank
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PackageStatus status;
}