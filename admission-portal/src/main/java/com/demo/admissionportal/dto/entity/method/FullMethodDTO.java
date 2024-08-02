package com.demo.admissionportal.dto.entity.method;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FullMethodDTO {
    private Integer id;
    private String name;
    private String code;
    private String createTime;
    private ActionerDTO createBy;
    private String updateTime;
    private ActionerDTO updateBy;
}
