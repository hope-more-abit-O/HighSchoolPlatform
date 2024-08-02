package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.CampusType;
import com.demo.admissionportal.constants.UniversityCampusStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type University campus.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "university_campus")
public class UniversityCampus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "university_id")
    private Integer universityId;

    @Column(name = "campus_name")
    private String campusName;

    @Column(name = "email")
    private String email;

    @Column(name = "specific_address")
    private String specificAddress;

    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "ward_id")
    private Integer wardId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "picture")
    private String picture;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CampusType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UniversityCampusStatus status;
}
