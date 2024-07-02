package com.demo.admissionportal.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

/**
 * The type User info.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class UserInfo {
    @Id
    @Column(name = "user_id")
    private Integer id;

    @NotNull
    @Nationalized
    @Column(name = "firstname")
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
}