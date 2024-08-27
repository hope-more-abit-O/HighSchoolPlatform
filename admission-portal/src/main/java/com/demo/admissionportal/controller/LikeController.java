package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.like.LikeResponseDTO;
import com.demo.admissionportal.dto.response.like.TotalLikeResponseDTO;
import com.demo.admissionportal.service.UserLikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Like controller.
 */
@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class LikeController {

    private final UserLikeService userLikeService;

    /**
     * Create like response entity.
     *
     * @param postID the post id
     * @return the response entity
     */
    @PostMapping("/{postID}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<LikeResponseDTO>> createLike(@PathVariable(name = "postID") Integer postID) {
        if (postID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "postId null"));
        }
        ResponseData<LikeResponseDTO> resultOfLike = userLikeService.createLike(postID);
        if (resultOfLike.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(resultOfLike);
        } else if (resultOfLike.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(resultOfLike);
        } else if (resultOfLike.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(resultOfLike);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(resultOfLike);
    }

    /**
     * Gets like.
     *
     * @param postId the post id
     * @return the like
     */
    @GetMapping("/postId={postId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<LikeResponseDTO>> getLike(@PathVariable(name = "postId") Integer postId) {
        if (postId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "postId null"));
        }
        ResponseData<LikeResponseDTO> favorite = userLikeService.getLike(postId);
        if (favorite.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(favorite);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
    }

    /**
     * Gets total.
     *
     * @param postId the post id
     * @return the total
     */
    @GetMapping("/total/{postId}")
    public ResponseEntity<ResponseData<TotalLikeResponseDTO>> getTotal(@PathVariable(name = "postId") Integer postId) {
        if (postId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "postId null"));
        }
        ResponseData<TotalLikeResponseDTO> response = userLikeService.getTotalLike(postId);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(response);
        } else if (response.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(response);
    }

    /**
     * Gets like by university.
     *
     * @param universityID the university id
     * @return the like by university
     */
    @GetMapping("/uniId={universityID}")
    public ResponseEntity<ResponseData<List<LikeResponseDTO>>> getLikeByUniversity(@PathVariable(name = "universityID") Integer universityID) {
        if (universityID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "postId null"));
        }
        ResponseData<List<LikeResponseDTO>> like = userLikeService.getLikeByUniversity(universityID);
        if (like.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(like);
        } else if (like.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(like);
        } else if (like.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(like);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(like);
    }
}
