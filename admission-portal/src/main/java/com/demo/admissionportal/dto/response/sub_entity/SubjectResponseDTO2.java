package com.demo.admissionportal.dto.response.sub_entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class SubjectResponseDTO2 {
    private Integer id;
    private String name;
    private String status;
    private Date createTime;

    public SubjectResponseDTO2(Integer id, String name, String status, Date createTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createTime = createTime;
    }
}