package com.demo.admissionportal.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostFavoriteResponseDTO implements Serializable {
    private PostPropertiesResponseDTO postProperties;
    private UserInfoPostResponseDTO info;
    private String publishAgo;
}
