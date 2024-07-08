//package com.demo.admissionportal.controller;
//
//import com.demo.admissionportal.dto.request.RequestSubjectDTO;
//import com.demo.admissionportal.dto.response.ResponseData;
//import com.demo.admissionportal.entity.Subject;
//import com.demo.admissionportal.service.SubjectService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
///**
// * The type Subject controller.
// */
//@RestController
//@RequestMapping("/api/v1/subject")
//@RequiredArgsConstructor
//@SecurityRequirement(name = "BearerAuth")
//public class SubjectController {
//    private final SubjectService subjectService;
//
//    /**
//     * Create subject response data.
//     *
//     * @param requestSubjectDTO the request subject dto
//     * @return the response data
//     */
//    @PostMapping()
//    public ResponseEntity<ResponseData<Subject>> createSubject(@RequestBody @Valid RequestSubjectDTO requestSubjectDTO) {
//        ResponseData<Subject> createdSubject = subjectService.createSubject(requestSubjectDTO);
//        if (createdSubject != null) {
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdSubject);
//        } else {
//            return ResponseEntity.status(createdSubject.getStatus()).body(createdSubject);
//        }
//    }
//}