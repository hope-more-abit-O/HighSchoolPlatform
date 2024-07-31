package com.demo.admissionportal.dto.entity.staff;

import com.demo.admissionportal.constants.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllStaffDTO {
    private Integer id;
    private String username;
    private String email;
    private String name;
    private String avatar;
    private String phone;
    private AccountStatus status;
    private String provinceName;
    private String note;
    private Date createTime;
}
