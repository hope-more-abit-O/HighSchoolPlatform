package com.demo.admissionportal.dto.response.post;

import com.demo.admissionportal.constants.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Post random response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostRandomResponseDTO implements Serializable {
    private Integer id;
    private String title;
    private String thumnail;
    private String quote;
    private PostStatus status;
    private Date create_time;
    private String url;
    private Integer like;
    private Integer replyComment;
    private String createBy;
}
