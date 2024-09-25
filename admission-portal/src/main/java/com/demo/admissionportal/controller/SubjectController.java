package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/all")
    public ResponseEntity<ResponseData> findAllSubjects() {
        return ResponseEntity.ok(ResponseData.ok("Lấy tất cả các môn học thành công.", subjectService.getAllActive()));
    }

    @GetMapping("/high-school-exam")
    public ResponseEntity<?> getHighSchoolExamSubjects() {
        return ResponseEntity.ok(ResponseData.ok("Lấy tất cả các môn thi tốt nghiệp THPT thành công.", subjectService.getHighSchoolExamSubjects()));
    }
}
