package com.demo.admissionportal.dto.request.post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Post request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class PostRequestDTO implements Serializable {
    @NotBlank(message = "Tiêu đề không được trống")
    private String title;

    @NotBlank(message = "Nội dung không được trống")
    private String content;

    @NotBlank(message = "Thumnail không được trống")
    private String thumnail;

    @NotBlank(message = "Quote không được trống")
    private String quote;

    @NotBlank(message = "Loại post không được trống")
    @Size(min = 1, message = "Tối thiểu từ 1 loại post")
    @Size(max = 3, message = "Tối đa 3 loại post")
    private List<Integer> listType;

    @NotBlank(message = "Tag của post không được trống")
    @Size(min = 1, message = "Tối thiểu 1 tag post")
    @Size(max = 3, message = "Tối đa 3 tag post")
    private List<String> listTag;
}
