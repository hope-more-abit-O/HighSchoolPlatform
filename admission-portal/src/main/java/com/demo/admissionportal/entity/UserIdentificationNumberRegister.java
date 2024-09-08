package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.IdentificationNumberRegisterStatus;
import com.demo.admissionportal.entity.sub_entity.id.UserIdentificationNumberId;
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
    @EmbeddedId
    private UserIdentificationNumberId id;
    @Column(name = "email")
    private String email;
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

    public Integer getUserId() {
        return id != null ? id.getUserId() : null;
    }
    public void setUserId(Integer userId) {
        if (this.id == null) {
            this.id = new UserIdentificationNumberId();
        }
        this.id.setUserId(userId);
    }
    public String getIdentificationNumber() {
        return id != null ? id.getIdentificationNumber() : null;
    }

    public void setIdentificationNumber(String identificationNumber) {
        if (this.id == null) {
            this.id = new UserIdentificationNumberId();
        }
        this.id.setIdentificationNumber(identificationNumber);
    }
}
