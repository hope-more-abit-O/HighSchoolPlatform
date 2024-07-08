//package com.demo.admissionportal.controller;
//
//import com.demo.admissionportal.constants.ResponseCode;
//import com.demo.admissionportal.dto.request.CreateSubjectGroupRequestDTO;
//import com.demo.admissionportal.dto.request.UpdateSubjectGroupRequestDTO;
//import com.demo.admissionportal.dto.response.ResponseData;
//import com.demo.admissionportal.dto.response.SubjectGroupResponseDTO;
//import com.demo.admissionportal.service.SubjectGroupService;
//import jakarta.validation.Valid;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * The type Subject group controller.
// */
//@RestController
//@RequestMapping("/api/v1/subject-group")
//@AllArgsConstructor
//public class SubjectGroupController {
//    @Autowired
//    private SubjectGroupService subjectGroupService;
//
//    @PostMapping("/create")
//    public ResponseEntity<ResponseData<?>> createSubjectGroup(@Valid @RequestBody CreateSubjectGroupRequestDTO request) {
//        if (request == null || request.isEmpty()) {
//            return ResponseEntity.badRequest().body(new ResponseData<>(ResponseCode.C205.getCode(), "Request cannot be null or empty"));
//        }
//        ResponseData<?> response = subjectGroupService.createSubjectGroup(request);
//        // If request is successful
//        if (response.getStatus() == ResponseCode.C200.getCode()) {
//            return ResponseEntity.ok(response);
//        }
//        // If duplicate fields are found
//        else if (response.getStatus() == ResponseCode.C204.getCode()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
//        }
//        // For other errors
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ResponseData<?>> updateSubjectGroup(@PathVariable Integer id, @RequestBody UpdateSubjectGroupRequestDTO request) {
//        ResponseData<?> response = subjectGroupService.updateSubjectGroup(id, request);
//        if (response.getStatus() == ResponseCode.C200.getCode()) {
//            return ResponseEntity.status(HttpStatus.OK).body(response);
//        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ResponseData<?>> getSubjectGroupById(@PathVariable Integer id) {
//        ResponseData<?> response = subjectGroupService.getSubjectGroupById(id);
//        if (response.getStatus() == ResponseCode.C200.getCode()) {
//            return ResponseEntity.status(HttpStatus.OK).body(response);
//        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//    @GetMapping
//    public ResponseEntity<ResponseData<Page<SubjectGroupResponseDTO>>> findAll(
////            @RequestParam(defaultValue = "") String name,
////            @RequestParam(defaultValue = "") String subjectName,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by("create_time").descending());
//        ResponseData<Page<SubjectGroupResponseDTO>> result = subjectGroupService.findAll(pageable);
//        if (result.getStatus() == ResponseCode.C200.getCode()) {
//            return ResponseEntity.ok(result);
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
//    }
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteSubjectGroup(@PathVariable @Valid Integer id){
//        ResponseData<?> response = subjectGroupService.deleteSubjectGroup(id);
//        if (response.getStatus() == ResponseCode.C200.getCode()) {
//            return ResponseEntity.status(HttpStatus.OK).body(response);
//        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//}
