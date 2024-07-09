package com.demo.admissionportal.dto.request.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Post request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO implements Serializable {
    private String title;
    private String content;
    private String thumnail;
    private String quote;

}
