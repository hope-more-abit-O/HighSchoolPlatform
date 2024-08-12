package com.demo.admissionportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StaffResponseDTO implements Serializable {
    private Integer id;
    private String username;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String avatar;
    private String phone;
    private String status;
    private String provinceName;
    private String note;
    private Date createTime;
    private String role;
}

