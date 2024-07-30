package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FullMajorDTO {
    private Integer id;
    private String name;
    private String code;
    private String createTime;
    private ActionerDTO createBy;
    private String updateTime;
    private ActionerDTO updateBy;
}
