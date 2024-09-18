package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.follow.*;
import com.demo.admissionportal.service.FollowService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Follow controller.
 */
@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    /**
     * Create follow response entity.
     *
     * @param majorId the major id
     * @return the response entity
     */
    @PostMapping("/major/{majorId}")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<FollowResponseDTO>> createFollow(@PathVariable(name = "majorId") Integer majorId) {
        if (majorId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "majorId null"));
        }
        ResponseData<FollowResponseDTO> favorite = followService.createFollowMajor(majorId);
        if (favorite.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
    }

    /**
     * Gets follow.
     *
     * @param majorId the major id
     * @return the follow
     */
    @GetMapping("/major/{majorId}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<FollowResponseDTO>> getFollow(@PathVariable(name = "majorId") Integer majorId) {
        if (majorId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "majorId null"));
        }
        ResponseData<FollowResponseDTO> favorite = followService.getFollowMajor(majorId);
        if (favorite.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(favorite);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
    }

    /**
     * Gets favorite by user id.
     *
     * @return the favorite by user id
     */
    @GetMapping("/major/list")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<List<UserFollowMajorResponseDTO>>> getFollowMajorByUserId() {
        ResponseData<List<UserFollowMajorResponseDTO>> list = followService.getListFollowMajor();
        if (list.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(list);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(list);
    }

    /**
     * Create follow uni major response entity.
     *
     * @param universityMajorId the university major id
     * @return the response entity
     */
    @PostMapping("/university/major/{universityMajorId}")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<FollowUniMajorResponseDTO>> createFollowUniMajor(@PathVariable(name = "universityMajorId") Integer universityMajorId) {
        if (universityMajorId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "majorId null"));
        }
        ResponseData<FollowUniMajorResponseDTO> favorite = followService.createFollowUniMajor(universityMajorId);
        if (favorite.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
    }

    /**
     * Gets follow university major.
     *
     * @param universityMajorId the university major id
     * @return the follow university major
     */
    @GetMapping("/university/major/{universityMajorId}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<FollowUniMajorResponseDTO>> getFollowUniversityMajor(@PathVariable(name = "universityMajorId") Integer universityMajorId) {
        if (universityMajorId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "majorId null"));
        }
        ResponseData<FollowUniMajorResponseDTO> followUniMajor = followService.getFollowUniMajor(universityMajorId);
        if (followUniMajor.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(followUniMajor);
        } else if (followUniMajor.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(followUniMajor);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(followUniMajor);
    }

    /**
     * Gets follow uni major by user id.
     *
     * @return the follow uni major by user id
     */
    @GetMapping("/university/major/list")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<List<UserFollowUniversityMajorResponseDTO>>> getFollowUniMajorByUserId(@RequestParam(required = true) Integer year) {
        ResponseData<List<UserFollowUniversityMajorResponseDTO>> list = followService.getListFollowUniMajor(year);
        if (list.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(list);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(list);
    }

    /**
     * Gets list user follow major.
     *
     * @return the list user follow major
     */
    @GetMapping("/university/major/list-users")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAnyAuthority('UNIVERSITY','CONSULTANT')")
    public ResponseEntity<ResponseData<List<UsersFollowMajorResponseDTO>>> getListUserFollowMajor() {
        ResponseData<List<UsersFollowMajorResponseDTO>> list = followService.getListUserFollowMajor();
        if (list.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(list);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(list);
    }
}
