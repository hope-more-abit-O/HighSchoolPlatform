package com.demo.admissionportal.dto.response.post;

import com.demo.admissionportal.constants.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The type Post response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostResponseDTO implements Serializable {
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
    private Integer create_by;
    private List<TypeResponseDTO> listType;
    private List<TagResponseDTO> listTag;

    /**
     * The Post Type response dto.
     */
    @Data
    @Builder
    public static class TypeResponseDTO implements Serializable {
        private Integer id;
        private String name;
    }

    /**
     * The type Tag response dto.
     */
    @Data
    @Builder
    public static class TagResponseDTO implements Serializable {
        private Integer id;
        private String name;
    }
}
