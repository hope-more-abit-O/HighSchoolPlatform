package com.demo.admissionportal.dto.response.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type User follow university major response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserFollowUniversityMajorResponseDTO implements Serializable {
    private Integer index;
    private Integer universityId;
    private Integer universityMajorId;
    private String universityName;
    private String universityCode;
    private String universityType;
    private String region;
    private String avatar;
    private String majorName;
    private String majorCode;
    private String training_specific;
    private String language;
    private Date createTime;
    private String subjectGroups;
}
