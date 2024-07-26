package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.MajorStatus;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/major")
@RequiredArgsConstructor
public class MajorController {
    private final MajorService majorService;

    @GetMapping
    public ResponseData<Page<Major>> getAllMajors(
            Pageable pageable,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) MajorStatus status,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) Integer updateBy,
            @RequestParam(required = false) Date createTime,
            @RequestParam(required = false) Date updateTime) {
        return majorService.getAllMajors(pageable, id, code, name, note, status, createBy, updateBy, createTime, updateTime);
    }
}
