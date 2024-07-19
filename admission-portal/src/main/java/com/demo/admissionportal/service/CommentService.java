package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.comment.CommentRequestDTO;
import com.demo.admissionportal.dto.request.comment.ReplyCommentRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.comment.CommentResponseDTO;

/**
 * The interface Comment service.
 */
public interface CommentService {
    /**
     * Create commment response data.
     *
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<?> createComment(CommentRequestDTO requestDTO);

    /**
     * Create reply comment response data.
     *
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<?> createReplyComment(ReplyCommentRequestDTO requestDTO);

    /**
     * Gets comments.
     *
     * @param postId the post id
     * @return the comments
     */
    ResponseData<CommentResponseDTO> getComments(Integer postId);
}
