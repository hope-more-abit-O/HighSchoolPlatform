package com.demo.admissionportal.dto.entity.search_engine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSearchDTO implements Serializable {
    private String title;
    private Date createTime;
    private String quote;
    private String thumnail;
    private String url;
}
