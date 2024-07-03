package com.demo.admissionportal.dto.entity.university;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversityResponseDTO {
    private Integer id;

    private String universityName;

    private String universityCode;

    private String universityEmail;

    private String universityUsername;

    private UniversityType universityType;

    private String note;

    private List<String> documents;

    private Date createTime;

    private ActionerDTO createBy;

    private Date updateTime;

    private ActionerDTO updateBy;

    private AccountStatus status;
}
