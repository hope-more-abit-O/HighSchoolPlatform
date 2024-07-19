package com.demo.admissionportal.dto.request.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Update post request dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostRequestDTO implements Serializable {
    private Integer postId;
    @NotNull(message = "Tiêu đề không được trống")
    private String title;

    @NotNull(message = "Nội dung không được trống")
    private String content;

    @NotNull(message = "Thumnail không được trống")
    private String thumnail;

    @NotNull(message = "Quote không được trống")
    private String quote;

    @NotNull(message = "Loại post không được trống")
    @Size(min = 1, message = "Tối thiểu từ 1 loại post")
    @Size(max = 3, message = "Tối đa 3 loại post")
    private List<Integer> listType;

    @NotNull(message = "Tag của post không được trống")
    @Size(min = 1, message = "Tối thiểu 1 tag post")
    @Size(max = 3, message = "Tối đa 3 tag post")
    private List<String> listTag;
}
