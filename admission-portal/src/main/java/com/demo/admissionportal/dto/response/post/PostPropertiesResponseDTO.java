package com.demo.admissionportal.dto.response.post;

import com.demo.admissionportal.constants.PostStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String thumnail;
    private String quote;
    private PostStatus status;
    private Date create_time;
    private String url;
    private String content;
    private Integer like;
    private Integer view;
}