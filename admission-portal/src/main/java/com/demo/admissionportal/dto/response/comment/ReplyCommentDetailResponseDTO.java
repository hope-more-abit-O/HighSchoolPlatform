package com.demo.admissionportal.dto.response.comment;

import com.demo.admissionportal.constants.CommentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Reply comment detail response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReplyCommentDetailResponseDTO implements Serializable {
    private Integer replayComment_id;
    private UserDetailCommentResponseDTO user_id;
    private String content;
    private String create_time;
    private CommentType comment_type;
}
