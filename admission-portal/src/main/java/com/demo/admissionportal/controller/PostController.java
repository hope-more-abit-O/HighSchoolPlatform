package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.PostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.PostRequestDTO;
import com.demo.admissionportal.dto.request.post.UpdatePostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.post.PostResponseDTO;
import com.demo.admissionportal.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Post controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    /**
     * Create post response entity.
     *
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('STAFF','CONSTULTANT')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<PostResponseDTO>> createPost(@RequestBody @Valid PostRequestDTO requestDTO) {
        if (requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Sai request"));
        }
        ResponseData<PostResponseDTO> responseData = postService.createPost(requestDTO);
        if (responseData.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
    }

    /**
     * Delete post response entity.
     *
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PostMapping("/change-status")
    @PreAuthorize("hasAnyAuthority('STAFF','CONSTULTANT')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<String>> deletePost(@RequestBody @Valid PostDeleteRequestDTO requestDTO) {
        if (requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Sai request"));
        }
        ResponseData<String> response = postService.changeStatus(requestDTO);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Update post response entity.
     *
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('STAFF','CONSTULTANT')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<String>> updatePost(@RequestBody @Valid UpdatePostRequestDTO requestDTO) {
        if (requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Sai request"));
        }
        ResponseData<String> responseData = postService.updatePost(requestDTO);
        if (responseData.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
    }

    /**
     * Gets posts.
     *
     * @return the posts
     */
    @GetMapping
    public ResponseEntity<ResponseData<List<PostResponseDTO>>> getPosts() {
        ResponseData<List<PostResponseDTO>> response = postService.getPosts();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<PostResponseDTO>> getPostsById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Không có id"));
        }
        ResponseData<PostResponseDTO> response = postService.getPostsById(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}