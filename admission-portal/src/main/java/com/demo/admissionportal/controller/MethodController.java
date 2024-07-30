package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.MethodStatus;
import com.demo.admissionportal.dto.request.method.PostMethodRequest;
import com.demo.admissionportal.dto.request.method.PutMethodRequest;
import com.demo.admissionportal.dto.request.method.UpdateMethodStatusRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.service.MethodService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/method")
@RequiredArgsConstructor
public class MethodController {
    private final MethodService methodService;

    @GetMapping
    public ResponseEntity<ResponseData<Page<Method>>> getAllMethods(
            Pageable pageable,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) Integer updateBy,
            @RequestParam(required = false) Date createTime,
            @RequestParam(required = false) Date updateTime,
            @RequestParam(required = false) MethodStatus status) {
        return ResponseEntity.ok(methodService.getAllMethods(pageable, id, code, name, createTime, createBy, updateTime, updateBy, status));
    }

    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity addMethod(@RequestBody @Valid PostMethodRequest request) {
        return ResponseEntity.ok(methodService.createMethod(request.getName(), request.getCode()));
    }

    @PutMapping
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity updateMethod(@RequestBody @Valid PutMethodRequest request) {
        return ResponseEntity.ok(methodService.updateMethod(request));
    }

    @PatchMapping
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity updateMethodStatus(@RequestBody @Valid UpdateMethodStatusRequest request) {
        return ResponseEntity.ok(methodService.updateMethodStatus(request));
    }
}
