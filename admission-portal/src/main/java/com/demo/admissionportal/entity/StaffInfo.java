package com.demo.admissionportal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * The type Staff info.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "staff_info")
public class StaffInfo {
    @Id
    @Column(name = "staff_id")
    private Integer id;

    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "province_id")
    private Integer provinceId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "staff_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    public StaffInfo(Integer id, Integer adminId, String firstName, String middleName, String lastName, String phone, Integer provinceId, User user) {
        this.id = id;
        this.adminId = adminId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.provinceId = provinceId;
    }

    public StaffInfo(Integer id, Integer adminId, String firstName, String middleName, String lastName, String phone, Integer provinceId) {
        this.id = id;
        this.adminId = adminId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.provinceId = provinceId;
    }
}
