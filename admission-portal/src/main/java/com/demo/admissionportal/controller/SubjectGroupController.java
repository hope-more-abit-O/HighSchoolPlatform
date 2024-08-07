package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO;
import com.demo.admissionportal.service.SubjectGroupService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subject-group")
@RequiredArgsConstructor
public class SubjectGroupController {
    private final SubjectGroupService subjectGroupService;

    @GetMapping("/all")
    public ResponseEntity<ResponseData<Page<SubjectGroupResponseDTO>>> findAllSubjectGroups(@RequestParam(required = false) String groupName, @RequestParam(required = false) String subjectName, @RequestParam(required = false) SubjectStatus status, Pageable pageable) {
        ResponseData<Page<SubjectGroupResponseDTO>> result = subjectGroupService.findAll(groupName, subjectName, status, pageable);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
