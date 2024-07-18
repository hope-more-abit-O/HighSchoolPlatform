package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.TypePostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.TypePostRequestDTO;
import com.demo.admissionportal.dto.request.post.TypePostUpdateRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Type;
import com.demo.admissionportal.service.TypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Type controller.
 */
@RestController
@RequestMapping("/api/v1/post/type")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
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

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('STAFF','CONSULTANT')")
    public ResponseEntity<ResponseData<List<Type>>> getTypePosts() {
        ResponseData<List<Type>> result = typeService.getListTypePost();
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
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

    @PostMapping("/change-status/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Type>> changeStatus(@PathVariable(name = "id") Integer postId, @RequestBody @Valid TypePostDeleteRequestDTO requestDTO) {
        if (postId == null || requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), "Không có postId"));
        }
        ResponseData<Type> result = typeService.changeStatus(postId, requestDTO);
        if (result.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else if (result.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
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
