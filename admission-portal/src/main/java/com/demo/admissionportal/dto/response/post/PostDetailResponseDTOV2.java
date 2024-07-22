package com.demo.admissionportal.dto.response.post;

import com.demo.admissionportal.constants.PostStatus;
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
public class PostDetailResponseDTOV2 implements Serializable {
    private Integer id;
    private String title;
    private String type;
    private String createBy;
    private Date createTime;
    private PostStatus status;
    private String note;
    private String url;
}
