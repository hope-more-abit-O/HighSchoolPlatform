package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.MethodStatus;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.service.impl.MethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/method")
@RequiredArgsConstructor
public class MethodController {
    private final MethodService methodService;

    @GetMapping
    public ResponseData<Page<Method>> getAllMethods(
            Pageable pageable,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) Integer updateBy,
            @RequestParam(required = false) Date createTime,
            @RequestParam(required = false) Date updateTime,
            @RequestParam(required = false) MethodStatus status) {
        return methodService.getAllMethods(pageable, id, code, name, createTime, createBy, updateTime, updateBy, status);
    }
}
