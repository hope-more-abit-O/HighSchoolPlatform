package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "consultant_info")
public class ConsultantInfo {
    @Id
    @Column(name = "consultant_id", nullable = false)
    private Integer id;

    @NotNull
    @Column
    private Integer universityId;

    @NotNull
    @Nationalized
    @Column(name = "firstname", nullable = false, length = 30)
    private String firstname;

    @NotNull
    @Nationalized
    @Column(name = "middle_name", nullable = false, length = 30)
    private String middleName;

    @NotNull
    @Nationalized
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "phone", length = 11)
    private String phone;

    @NotNull
    @Nationalized
    @Column(name = "specific_address", nullable = false)
    private String specificAddress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    private Ward ward;

    public ConsultantInfo(Integer id, Integer universityId, String firstname, String middleName, String lastName, String phone, String specificAddress, Gender gender, Province province, District district, Ward ward) {
        this.id = id;
        this.universityId = universityId;
        this.firstname = firstname;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.specificAddress = specificAddress;
        this.gender = gender;
        this.province = province;
        this.district = district;
        this.ward = ward;
    }

}