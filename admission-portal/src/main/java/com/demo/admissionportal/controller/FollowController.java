package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.favorite.FavoriteResponseDTO;
import com.demo.admissionportal.dto.response.follow.FollowResponseDTO;
import com.demo.admissionportal.service.FollowService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/major/{majorId}")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<FollowResponseDTO>> createFollow(@PathVariable(name = "majorId") Integer majorId) {
        if (majorId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "majorId null"));
        }
        ResponseData<FollowResponseDTO> favorite = followService.createFollow(majorId);
        if (favorite.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(favorite);
        } else if (favorite.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(favorite);
    }
}
