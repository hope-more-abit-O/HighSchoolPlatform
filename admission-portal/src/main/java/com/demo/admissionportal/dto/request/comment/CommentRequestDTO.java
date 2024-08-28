package com.demo.admissionportal.dto.request.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Valid
@NoArgsConstructor
public class CommentRequestDTO implements Serializable {
    @NotBlank(message = "postId không đuợc trống")
    private Integer post_id;
    @NotBlank(message = "Nội dung bình luận không đuợc trống")
    private String content;
}
