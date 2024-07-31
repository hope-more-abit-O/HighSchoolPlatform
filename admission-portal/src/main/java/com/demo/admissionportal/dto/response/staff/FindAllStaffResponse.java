package com.demo.admissionportal.dto.response.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllStaffResponse {
    private Integer id;
    private String username;
    private String email;
    private String name;
    private String avatar;
    private String phone;
    private String status;
    private String provinceName;
    private String note;
    private Date createTime;
}
