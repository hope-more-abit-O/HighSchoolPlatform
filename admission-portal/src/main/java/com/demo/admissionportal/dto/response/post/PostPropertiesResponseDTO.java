package com.demo.admissionportal.dto.response.post;

import com.demo.admissionportal.constants.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Post properties response dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostPropertiesResponseDTO implements Serializable {
    private Integer id;
    private String title;
    private String content;
    private String thumnail;
    private String quote;
    private Integer view;
    private Integer like;
    private PostStatus status;
    private Date create_time;
    private Date update_time;
    private Integer update_by;
    private String url;
}