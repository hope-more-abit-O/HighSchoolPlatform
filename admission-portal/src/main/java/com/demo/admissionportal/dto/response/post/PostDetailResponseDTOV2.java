package com.demo.admissionportal.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDetailResponseDTOV2 implements Serializable {
    private Integer id;
    private String title;
    private String type;
    private String createBy;
    private Date createTime;
    private String status;
    private String note;
    private String url;
    private Date startCampaignDate;
    private Date endCampaignDate;
    private String packageName;
}
