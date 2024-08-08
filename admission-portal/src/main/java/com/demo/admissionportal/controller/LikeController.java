package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.like.LikeResponseDTO;
import com.demo.admissionportal.service.UserLikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{universityID}")
    public ResponseEntity<ResponseData<LikeResponseDTO>> getLike(@PathVariable(name = "universityID") Integer universityID) {
        if (universityID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "postId null"));
        }
        ResponseData<LikeResponseDTO> favorite = userLikeService.getLike(universityID);
        if (favorite.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(favorite);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
    }
}
