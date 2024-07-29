package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.MajorStatus;
import com.demo.admissionportal.dto.request.major.CreateMajorRequest;
import com.demo.admissionportal.dto.request.major.UpdateMajorRequest;
import com.demo.admissionportal.dto.request.major.UpdateMajorStatusRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.service.MajorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/major")
@RequiredArgsConstructor
public class MajorController {
    private final MajorService majorService;

    @GetMapping
    public ResponseEntity<ResponseData<Page<Major>>> getAllMajors(
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
        return ResponseEntity.ok(majorService.getAllMajorsInfo(pageable, id, code, name, note, status, createBy, updateBy, createTime, updateTime));
    }

    @PostMapping
    public ResponseEntity<ResponseData> createMajor(@RequestBody @Valid CreateMajorRequest request) {
        return ResponseEntity.ok(ResponseData.ok("Tạo ngành thành công.",majorService.createMajor(request)));
    }

    @PutMapping
    public ResponseEntity<ResponseData> updateMajor(@RequestBody @Valid UpdateMajorRequest request){
        return ResponseEntity.ok(ResponseData.ok("Chỉnh sửa ngành thành công.", majorService.updateMajor(request)));
    }

    @PatchMapping
    public ResponseEntity<ResponseData> updateMajorStatus(@RequestBody @Valid UpdateMajorStatusRequest request){
        return ResponseEntity.ok(majorService.updateMajorStatus(request));
    }
}
