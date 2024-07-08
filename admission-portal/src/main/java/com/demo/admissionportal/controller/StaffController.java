package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.request.*;
import com.demo.admissionportal.dto.response.*;
import com.demo.admissionportal.dto.response.sub_entity.SubjectResponseDTO;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.service.StaffService;
import com.demo.admissionportal.service.SubjectGroupService;
import com.demo.admissionportal.service.SubjectService;
import com.demo.admissionportal.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * The type Staff controller.
 */
@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "BearerAuth")
public class StaffController {
    private final StaffService staffService;
    private final UserService userService;
    private final SubjectGroupService subjectGroupService;
    private final SubjectService subjectService;

    /**
     * Gets staff by id.
     *
     * @param id the id
     * @return the staff by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffById(@PathVariable int id) {
        ResponseData<?> result = staffService.getStaffById(id);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff Get by ID successfully: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found by ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to Get staff by ID: {}", id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Update staff response entity.
     *
     * @param request the request
     * @param id      the id
     * @return the response entity
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseData<StaffResponseDTO>> updateStaff(@RequestBody @Valid UpdateStaffRequestDTO request, @PathVariable Integer id) {
        log.info("Received request to update staff: {}", request);
        ResponseData<StaffResponseDTO> result = staffService.updateStaff(request, id);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff updated successfully with ID: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to update staff: {}", request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @param email    the email
     * @param pageable the pageable
     * @return the user
     */
    @GetMapping("/list/users")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<UserResponseDTO>>> getUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            Pageable pageable
    ) {
        ResponseData<Page<UserResponseDTO>> user = userService.getUser(username, email, pageable);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }

    /**
     * Change status response entity.
     *
     * @param id         the id
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PostMapping("/user/{id}/change-status")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<ChangeStatusUserRequestDTO>> changeStatus(@PathVariable("id") Integer id, @RequestBody ChangeStatusUserRequestDTO requestDTO) {
        if (id == null || id < 0 || requestDTO == null) {
            new ResponseEntity<ResponseData<ChangeStatusUserRequestDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<ChangeStatusUserRequestDTO> user = userService.changeStatus(id, requestDTO);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }
    @GetMapping("/list-all-subjects")
    public ResponseEntity<ResponseData<Page<SubjectResponseDTO>>> findAllSubjects(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) SubjectStatus status,
            Pageable pageable) {

        ResponseData<Page<SubjectResponseDTO>> result = subjectService.findAll(name, status, pageable);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
    @GetMapping("/get-subject/{id}")
    public ResponseEntity<ResponseData<Subject>> getSubjectById(@PathVariable Integer id) {
        ResponseData<Subject> response = subjectService.getSubjectById(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/create-subject")
    public ResponseEntity<ResponseData<Subject>> createSubject(@RequestBody @Valid RequestSubjectDTO requestSubjectDTO) {
        ResponseData<Subject> createdSubject = subjectService.createSubject(requestSubjectDTO);
        if (createdSubject != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSubject);
        } else {
            return ResponseEntity.status(createdSubject.getStatus()).body(createdSubject);
        }
    }
    @PutMapping("/activate-subject/{id}")
    public ResponseEntity<?> activateSubject(@PathVariable @Valid Integer id) {
        ResponseData<?> response = subjectService.activateSubject(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @DeleteMapping("/delete-subject/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable @Valid Integer id){
        ResponseData<?> response = subjectService.deleteSubject(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/create-subject-group")
    public ResponseEntity<ResponseData<?>> createSubjectGroup(@Valid @RequestBody CreateSubjectGroupRequestDTO request) {
        if (request == null ) {
            return ResponseEntity.badRequest().body(new ResponseData<>(ResponseCode.C205.getCode(), "Request cannot be null or empty"));
        }
        ResponseData<?> response = subjectGroupService.createSubjectGroup(request);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PutMapping("/update-subject-group/{id}")
    public ResponseEntity<ResponseData<?>> updateSubjectGroup(@PathVariable Integer id, @RequestBody UpdateSubjectGroupRequestDTO request) {
        ResponseData<?> response = subjectGroupService.updateSubjectGroup(id, request);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get-subject-group/{id}")
    public ResponseEntity<ResponseData<?>> getSubjectGroupById(@PathVariable Integer id) {
        ResponseData<?> response = subjectGroupService.getSubjectGroupById(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/list-all-subject-groups")
    public ResponseEntity<ResponseData<Page<SubjectGroupResponseDTO>>> findAllSubjectGroups(
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        ResponseData<Page<SubjectGroupResponseDTO>> result = subjectGroupService.findAll(groupName, subjectName, status, pageable);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
    @PutMapping("/activate-subject-group/{id}")
    public ResponseEntity<ResponseData<?>> activateSubjectGroup(@PathVariable Integer id) {
        ResponseData<?> result = subjectGroupService.activateSubjectGroup(id);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
    @DeleteMapping("/delete-subject-group/{id}")
    public ResponseEntity<?> deleteSubjectGroup(@PathVariable @Valid Integer id){
        ResponseData<?> response = subjectGroupService.deleteSubjectGroup(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
