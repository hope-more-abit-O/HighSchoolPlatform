package com.demo.admissionportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.Date;

/**
 * The type User info.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "[user_info]")
public class UserInfo implements Serializable {
    @Id
    @Column(name = "user_id")
    private Integer id;

    @NotNull
    @Nationalized
    @Column(name = "first_name")
    private String firstname;

    @NotNull
    @Nationalized
    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Nationalized
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @NotNull
    @Nationalized
    @Column(name = "gender")
    private String gender;

    @NotNull
    @Nationalized
    @Column(name = "specific_address")
    private String specificAddress;

    @NotNull
    @Nationalized
    @Column(name = "education_level")
    private String educationLevel;

    @Column(name = "province_id")
    private Integer province;

    @Column(name = "district_id")
    private Integer district;

    @Column(name = "ward_id")
    private Integer ward;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}