package com.demo.admissionportal.dto.response.sub_entity;

import com.demo.admissionportal.constants.SubjectStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
public class CreateSubjectResponseDTO {
    private Integer id;
    private String name;
    private String status;
    private Date createTime;
    private Integer createBy;
    @JsonIgnore
    private Date updateTime;
    @JsonIgnore
    private Integer updateBy;

    public CreateSubjectResponseDTO(Integer id, String name, Date createTime, Integer createBy, Integer updateBy, String name1) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.status = SubjectStatus.ACTIVE.name();
    }
}
