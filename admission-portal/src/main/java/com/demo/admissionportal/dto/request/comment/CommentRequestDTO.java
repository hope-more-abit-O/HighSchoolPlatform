package com.demo.admissionportal.dto.request.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Valid
@NoArgsConstructor
public class CommentRequestDTO implements Serializable {
    @NotNull(message = "postId không đuợc trống")
    private Integer post_id;
    @NotNull(message = "Nội dung bình luận không đuợc trống")
    private String content;
}
