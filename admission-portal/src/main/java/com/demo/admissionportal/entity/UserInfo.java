package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Table(name = "[user_info]")
public class UserInfo implements Serializable {
    @Id
    @Column(name = "user_id")
    private Integer id;

    @NotNull
    @Nationalized
    @Column(name = "first_name")
    private String firstName;

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
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Nationalized
    @Column(name = "specific_address")
    private String specificAddress;

    @NotNull
    @Nationalized
    @Column(name = "education_level")
    private String educationLevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "identification_number")
    private Integer identificationNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}