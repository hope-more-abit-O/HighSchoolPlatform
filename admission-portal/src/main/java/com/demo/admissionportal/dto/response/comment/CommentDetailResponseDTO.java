package com.demo.admissionportal.dto.response.comment;

import com.demo.admissionportal.constants.CommentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Comment detail response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentDetailResponseDTO implements Serializable {
    private Integer id;
    private Integer postId;
    private String content;
    private String create_time;
    private UserDetailCommentResponseDTO user_id;
    private CommentType comment_type;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ReplyCommentDetailResponseDTO> replyComment;
}
