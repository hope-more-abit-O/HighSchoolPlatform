package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.constants.UniversityType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class PostCreateUniversityRequestResponse {
    private Integer id;

    private String universityName;

    private String universityCode;

    private String universityEmail;

    private String universityUsername;

    private UniversityType universityType;

    private String note;

    private List<String> documents;

    private Date createTime;

    private Integer createBy;

    private Date updateTime;

    private Integer updateBy;

    private String status;
}
