package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.PostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.sub_entity.PostType;
import com.demo.admissionportal.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ResponseData<List<PostType>>> createPost(@RequestBody @Valid PostRequestDTO requestDTO) {
        if (requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Sai request"));
        }
        ResponseData<List<PostType>> responseData = postService.createPost(requestDTO);
        if (responseData.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } else if (responseData.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(responseData);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
    }
}
