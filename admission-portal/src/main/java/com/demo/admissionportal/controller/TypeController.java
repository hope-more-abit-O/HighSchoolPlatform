package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.TypePostRequestDTO;
import com.demo.admissionportal.dto.request.post.TypePostUpdateRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.type.TypeListResponseDTO;
import com.demo.admissionportal.entity.Type;
import com.demo.admissionportal.service.TypeService;
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

/**
 * The type Type controller.
 */
@RestController
@RequestMapping("/api/v1/post/type")
@RequiredArgsConstructor
public class TypeController {
    private final TypeService typeService;

    /**
     * Create type post response entity.
     *
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PostMapping
    @PreAuthorize("hasAuthority('STAFF')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<Type>> createTypePost(@RequestBody @Valid TypePostRequestDTO requestDTO) {
        if (requestDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<Type> result = typeService.createTypePost(requestDTO);
        if (result.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } else if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Gets type posts.
     *
     * @param typeName the type name
     * @param status   the status
     * @param pageable the pageable
     * @return the type posts
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseData<Page<TypeListResponseDTO>>> getTypePosts(@RequestParam(required = false) String typeName,
                                                                                @RequestParam(required = false) String status,
                                                                                @PageableDefault(size = 10) Pageable pageable) {
        ResponseData<Page<TypeListResponseDTO>> result = typeService.getListTypePost(typeName, status, pageable);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Gets type by id.
     *
     * @param postId the post id
     * @return the type by id
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<Type>> getTypeById(@PathVariable(name = "id") Integer postId) {
        if (postId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Không có postId"));
        }
        ResponseData<Type> result = typeService.getTypeById(postId);
        if (result.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Change status response entity.
     *
     * @param typeId the type id
     * @return the response entity
     */
    @PostMapping("/change-status/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<Type>> changeStatus(@PathVariable(name = "id") Integer typeId) {
        if (typeId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Không có TypeId"));
        }
        ResponseData<Type> result = typeService.changeStatus(typeId);
        if (result.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else if (result.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Update type response entity.
     *
     * @param postId     the post id
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<Type>> updateType(@PathVariable(name = "id") Integer postId, @RequestBody @Valid TypePostUpdateRequestDTO requestDTO) {
        if (postId == null || requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Thiếu request"));
        }
        ResponseData<Type> result = typeService.updateType(postId, requestDTO);
        if (result.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else if (result.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
