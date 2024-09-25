package com.demo.admissionportal.dto.response.sub_entity;

import com.demo.admissionportal.entity.Subject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class SubjectResponseDTO2 {
    private Integer id;
    private String name;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date createTime;

    public SubjectResponseDTO2(Integer id, String name, String status, Date createTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createTime = createTime;
    }

    public SubjectResponseDTO2(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.status = subject.getStatus().name();
    }
}