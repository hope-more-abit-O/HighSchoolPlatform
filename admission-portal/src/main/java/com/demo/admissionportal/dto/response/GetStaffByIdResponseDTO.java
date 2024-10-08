package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.dto.entity.ActionerDTO;
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
public class GetStaffByIdResponseDTO implements Serializable {
    private Integer id;
    private String username;
    private String email;
    private String name;
    private String avatar;
    private String phone;
    private String status;
    private Integer provinceId;
    private String note;
    private Date createTime;
    private ActionerDTO createBy;
}