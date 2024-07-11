package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.ActionerDTO;

import java.util.Date;

public class FullAdmissionDTO {
    private Integer admissionId;
    private Integer year;
    private Integer universityId;
    private ActionerDTO createBy;
    private String createTime;
    private ActionerDTO updateBy;
    private String updateTime;
    private String status;
}
