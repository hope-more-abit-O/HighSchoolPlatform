package com.demo.admissionportal.dto.response.post;

import com.demo.admissionportal.dto.entity.post.InfoPostResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PostFavoriteResponseDTO implements Serializable {
    private PostPropertiesResponseDTO postProperties;
    private InfoPostResponseDTO info;
    private Date publishAgo;
    @JsonIgnore
    private float interactPost;
}
