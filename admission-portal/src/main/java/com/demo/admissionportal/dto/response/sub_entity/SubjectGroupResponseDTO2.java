package com.demo.admissionportal.dto.response.sub_entity;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectGroupResponseDTO2 {
    private Integer id;
    private String name;
    private String status;

    public SubjectGroupResponseDTO2(Integer id, String name, String status, ActionerDTO createBy) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
}