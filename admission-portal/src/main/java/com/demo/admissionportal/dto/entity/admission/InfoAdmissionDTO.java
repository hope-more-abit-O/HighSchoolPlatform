package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.ActionerDTO;

import java.util.Date;

public class InfoAdmissionDTO {
    private Integer admissionId;
    private Integer year;
    private ActionerDTO createBy;
    private Date createTime;
    private String status;
}
