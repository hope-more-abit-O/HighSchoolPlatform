package com.demo.admissionportal.dto.response.sub_entity;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SubjectGroupResponseDTO {
    private Integer id;
    private String name;
    private String status;
    private ActionerDTO createBy;
    private Date createTime;
    private List<SubjectResponseDTO2> subjects;

    public SubjectGroupResponseDTO(Integer id, String name, String status, ActionerDTO createBy, List<SubjectResponseDTO2> subjects, Date createTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createBy = createBy;
        this.subjects = subjects;
        this.createTime = createTime;
    }
}
