package com.demo.admissionportal.dto.request.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Reply comment request dto.
 */
@Data
@AllArgsConstructor
@Valid
@NoArgsConstructor
public class ReplyCommentRequestDTO implements Serializable {
    @NotNull(message = "comment_parent_id không được trống")
    private Integer comment_parent_id;
    @NotNull(message = "postId không đuợc trống")
    private Integer post_id;
    @NotNull(message = "Nội dung bình luận không đuợc trống")
    private String content;
}
