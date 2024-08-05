package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.comment.CommentRequestDTO;
import com.demo.admissionportal.dto.request.comment.ReplyCommentRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.comment.CommentResponseDTO;
import com.demo.admissionportal.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Comment controller.
 */
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * Create comment response entity.
     *
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PostMapping()
    @PreAuthorize("hasAnyAuthority('USER','CONSULTANT','STAFF')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<?>> createComment(@RequestBody @Valid CommentRequestDTO requestDTO) {
        if (requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Sai request"));
        }
        ResponseData<?> createComment = commentService.createComment(requestDTO);
        if (createComment.getStatus() == ResponseCode.C206.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createComment);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createComment);
    }

    /**
     * Create reply comment response entity.
     *
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PostMapping("/reply")
    @PreAuthorize("hasAnyAuthority('USER','CONSULTANT','STAFF')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<?>> createReplyComment(@RequestBody @Valid ReplyCommentRequestDTO requestDTO) {
        if (requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Sai request"));
        }
        ResponseData<?> replyComment = commentService.createReplyComment(requestDTO);
        if (replyComment.getStatus() == ResponseCode.C206.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(replyComment);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(replyComment);
    }

    /**
     * Gets comments by post id.
     *
     * @param postId the post id
     * @return the comments by post id
     */
    @GetMapping("/{postId}")
    public ResponseEntity<ResponseData<List<CommentResponseDTO>>> getCommentsByPostId(@PathVariable(name = "postId") Integer postId) {
        if (postId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Sai request"));
        }
        ResponseData<List<CommentResponseDTO>> responseData = commentService.getCommentsByPostId(postId);
        if (responseData.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
    }
}
