package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.IdentificationNumberRegisterStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "[user_identification_register]")
public class UserIdentificationNumberRegister {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "email")
    private String email;
    @Column(name = "identification_number")
    private Integer identificationNumber;
    @Column(name = "year")
    private Integer year;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private IdentificationNumberRegisterStatus status;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "update_by")
    private Integer updateBy;
}