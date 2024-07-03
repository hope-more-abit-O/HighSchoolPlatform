package com.demo.admissionportal.dto.entity.consultant;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.entity.*;
import lombok.*;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantResponseDTO {
    private Integer id;
    private InfoUniversityResponseDTO university;
    private String email;
    private String username;
    private String avatar;
    private String name;
    private String phone;
    private String address;
    private String note;
    private Gender gender;
    private Role role;
    private Date createTime;
    private ActionerDTO createBy;
    private Date updateTime;
    private ActionerDTO updateBy;
    private AccountStatus status;

    public ConsultantResponseDTO(User account, ConsultantInfo consultantInfo, InfoUniversityResponseDTO university, ActionerDTO createBy, ActionerDTO updateBy) {
        this.id = account.getId();
        this.university = university;
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.avatar = account.getAvatar();
        this.name = consultantInfo.getFirstname() + " " + consultantInfo.getMiddleName() + " "  + consultantInfo.getLastName();
        this.phone = consultantInfo.getPhone();
        this.address = consultantInfo.getSpecificAddress() + ", " + consultantInfo.getWard().getName() + ", " + consultantInfo.getDistrict().getName() + ", " + consultantInfo.getProvince().getName();
        this.note = ""; //TODO: update
        this.gender = consultantInfo.getGender();
        this.role = account.getRole();
        this.createTime = account.getCreateTime();
        this.createBy = createBy;
        this.updateTime = account.getUpdateTime();
        this.updateBy = updateBy;
        this.status = account.getStatus();
    }
    public ConsultantResponseDTO(User account, ConsultantInfo consultantInfo, InfoUniversityResponseDTO university, ActionerDTO createBy) {
        this.id = account.getId();
        this.university = university;
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.avatar = account.getAvatar();
        this.name = consultantInfo.getFirstname() + " " + consultantInfo.getMiddleName() + " "  + consultantInfo.getLastName();
        this.phone = consultantInfo.getPhone();
        this.address = consultantInfo.getSpecificAddress() + ", " + consultantInfo.getWard().getName() + ", " + consultantInfo.getDistrict().getName() + ", " + consultantInfo.getProvince().getName();
        this.note = ""; //TODO: update
        this.gender = consultantInfo.getGender();
        this.role = account.getRole();
        this.createTime = account.getCreateTime();
        this.createBy = createBy;
        this.status = account.getStatus();
    }
}
