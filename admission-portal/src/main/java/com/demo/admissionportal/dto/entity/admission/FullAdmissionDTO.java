package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
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
