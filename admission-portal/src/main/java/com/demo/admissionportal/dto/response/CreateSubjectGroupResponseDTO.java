package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.constants.SubjectStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateSubjectGroupResponseDTO {
    private Integer id;
    private String name;
    private String status;
    private Date createTime;
    private Integer createBy;
    @JsonIgnore
    private Date updateTime;
    @JsonIgnore
    private Integer updateBy;
    private List<CreateSubjectResponseDTO> subjects;

    public CreateSubjectGroupResponseDTO(Integer id, String name, Date createTime, Integer createBy, Integer updateBy, String name1) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.status = SubjectStatus.ACTIVE.name();
    }
}
