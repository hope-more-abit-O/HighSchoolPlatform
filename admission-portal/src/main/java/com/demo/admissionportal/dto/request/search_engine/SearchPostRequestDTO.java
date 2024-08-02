package com.demo.admissionportal.dto.request.search_engine;

import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPostRequestDTO implements Serializable {
    private PostSearchDTO post;
    private String createBy;
    private String avatar;
}
