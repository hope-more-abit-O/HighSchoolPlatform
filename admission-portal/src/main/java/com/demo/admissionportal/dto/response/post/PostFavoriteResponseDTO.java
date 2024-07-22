package com.demo.admissionportal.dto.response.post;

import com.demo.admissionportal.dto.entity.post.UniversityPostResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFavoriteResponseDTO implements Serializable {
    private PostPropertiesResponseDTO postProperties;
    private UniversityPostResponseDTO universityInfo;
    private String publishAgo;
}
