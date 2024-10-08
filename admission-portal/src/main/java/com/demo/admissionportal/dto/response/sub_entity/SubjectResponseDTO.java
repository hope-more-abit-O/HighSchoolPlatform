package com.demo.admissionportal.dto.response.sub_entity;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponseDTO {
    private Integer id;
    private String name;
    private String status;
    private ActionerDTO createBy;
    private Date createTime;

    public SubjectResponseDTO(Integer id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public SubjectResponseDTO(Integer id, String name, String name1, Date createTime) {

    }
}
