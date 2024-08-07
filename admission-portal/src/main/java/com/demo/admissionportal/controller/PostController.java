package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.PostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.PostRequestDTO;
import com.demo.admissionportal.dto.request.post.UpdatePostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.post.*;
import com.demo.admissionportal.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    @PreAuthorize("hasAnyAuthority('STAFF','CONSULTANT')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<PostDetailResponseDTO>> createPost(@RequestBody @Valid PostRequestDTO requestDTO) {
        if (requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Sai request"));
        }
        ResponseData<PostDetailResponseDTO> responseData = postService.createPost(requestDTO);
        if (responseData.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } else if (responseData.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
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
    @PreAuthorize("hasAnyAuthority('STAFF','CONSULTANT')")
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
    @PreAuthorize("hasAnyAuthority('STAFF','CONSULTANT')")
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
     * Gets posts by id.
     *
     * @param id the id
     * @return the posts by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<PostDetailResponseDTO>> getPostsById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Không có id"));
        }
        ResponseData<PostDetailResponseDTO> response = postService.getPostsById(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Gets posts newest.
     *
     * @param locationId the location id
     * @return the posts newest
     */
    @GetMapping("/newest")
    public ResponseEntity<ResponseData<List<PostResponseDTO>>> getPostsNewest(@RequestParam(required = false, name = "locationId") Integer locationId) {
        ResponseData<List<PostResponseDTO>> response = postService.getPostsNewest(locationId);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Gets posts general.
     *
     * @param locationId the location id
     * @return the posts general
     */
    @GetMapping("/general")
    public ResponseEntity<ResponseData<List<PostResponseDTO>>> getPostsGeneral(@RequestParam(required = false, name = "locationId") Integer locationId) {
        ResponseData<List<PostResponseDTO>> response = postService.getPostsGeneral(locationId);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * List posts response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/list/{userId}")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAnyAuthority('STAFF','CONSULTANT','UNIVERSITY')")
    public ResponseEntity<ResponseData<List<PostDetailResponseDTO>>> listPosts(@PathVariable("userId") Integer id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Không có id"));
        }
        ResponseData<List<PostDetailResponseDTO>> response = postService.listPostByConsultOrStaffOrUniId(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (response.getStatus() == ResponseCode.C209.getCode()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Gets posts list.
     *
     * @param title    the title
     * @param status   the status
     * @param pageable the pageable
     * @return the posts list
     */
    @GetMapping("/list")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAnyAuthority('STAFF','CONSULTANT','UNIVERSITY')")
    public ResponseEntity<ResponseData<Page<PostDetailResponseDTOV2>>> getPostsList(@RequestParam(required = false) String title,
                                                                                    @RequestParam(required = false) String status,
                                                                                    @PageableDefault(size = 10) Pageable pageable) {
        ResponseData<Page<PostDetailResponseDTOV2>> response = postService.listAllPostConsulOrStaff(title, status, pageable);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (response.getStatus() == ResponseCode.C209.getCode()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Gets posts favorite.
     *
     * @param locationId the location id
     * @return the posts favorite
     */
    @GetMapping("/favorite")
    public ResponseEntity<ResponseData<List<PostFavoriteResponseDTO>>> getPostsFavorite(@RequestParam(required = false, name = "locationId") Integer locationId) {
        ResponseData<List<PostFavoriteResponseDTO>> response = postService.listPostFavorite(locationId);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Gets post random.
     *
     * @param locationId the location id
     * @param pageable   the pageable
     * @return the post random
     */
    @GetMapping("/if-you-like")
    public ResponseEntity<ResponseData<Page<PostRandomResponseDTO>>> getPostRandom(@RequestParam(required = false, name = "locationId") Integer locationId, Pageable pageable) {
        ResponseData<Page<PostRandomResponseDTO>> response = postService.listPostRandom(locationId, pageable);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/URL={url}")
    public ResponseEntity<ResponseData<PostDetailResponseDTO>> getPostsByUrl(@PathVariable("url") String url) {
        if (url == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Không có url"));
        }
        ResponseData<PostDetailResponseDTO> response = postService.getPostsByURL(url.trim());
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/list/uni={universityId}")
    public ResponseEntity<ResponseData<List<PostDetailResponseDTOV3>>> getPostsByIdV2(@PathVariable("universityId") Integer universityId) {
        if (universityId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Không có id"));
        }
        ResponseData<List<PostDetailResponseDTOV3>> response = postService.getPostsByIdV2(universityId);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/has-package")
    public ResponseEntity<ResponseData<List<PostPackageResponseDTO>>> getPostHasPackage(){
        ResponseData<List<PostPackageResponseDTO>> response = postService.getPostHasPackage();
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
