package com.demo.admissionportal.dto.entity.major;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
