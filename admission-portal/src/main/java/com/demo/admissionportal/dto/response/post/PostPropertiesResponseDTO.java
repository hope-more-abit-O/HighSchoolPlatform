package com.demo.admissionportal.dto.response.post;

import com.demo.admissionportal.constants.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    private String create_time;
    private String url;
}