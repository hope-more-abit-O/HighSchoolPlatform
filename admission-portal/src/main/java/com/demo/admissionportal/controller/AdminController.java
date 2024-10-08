package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.create_university_request.CreateUniversityRequestDTO;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.request.ActiveStaffRequest;
import com.demo.admissionportal.dto.request.DeleteStaffRequest;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.ads_package.PackageResponseDTO;
import com.demo.admissionportal.dto.request.create_univeristy_request.CreateUniversityRequestAdminActionRequest;
import com.demo.admissionportal.dto.request.university.UpdateUniversityStatusRequest;
import com.demo.admissionportal.dto.response.RegisterStaffResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.staff.FindAllStaffResponse;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Admin controller.
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final StaffService staffService;
    private final CreateUniversityService createUniversityService;
    private final UniversityService universityService;
    private final UniversityTransactionService universityTransactionService;

    /**
     * Register staff response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/register-staff")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<RegisterStaffResponse>> registerStaff(@RequestBody @Valid RegisterStaffRequestDTO request) {
        ResponseData<RegisterStaffResponse> response = staffService.registerStaff(request);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Find all staff response entity.
     *
     * @param username the username
     * @param name     the name
     * @param email    the email
     * @param phone    the phone
     * @param status   the status
     * @param pageable the pageable
     * @return the response entity
     */
    @GetMapping("/list-all-staffs")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<Page<FindAllStaffResponse>>> findAllStaff(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String status,
            Pageable pageable) {

        AccountStatus accountStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                accountStatus = AccountStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseData<>(ResponseCode.C205.getCode(), "Trạng thái không hợp lệ"));
            }
        }

        ResponseData<Page<FindAllStaffResponse>> response = staffService.findAll(username, name, email, phone, accountStatus, pageable);
        return ResponseEntity.status(response.getStatus() == ResponseCode.C200.getCode() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    /**
     * Gets staff by id.
     *
     * @param id the id
     * @return the staff by id
     */
    @GetMapping("/get-staff/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
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
     * Delete staff by id response entity.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     */
    @DeleteMapping("/delete-staff/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteStaffById(@Valid @PathVariable int id, @Valid @RequestBody DeleteStaffRequest request) {
        log.info("Received request to delete staff with ID: {} and note: {}", id, request.note());
        ResponseData<?> result = staffService.deleteStaffById(id, request);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff deleted successfully with ID: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to delete staff with ID: {}", id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Activate staff by id response entity.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     */
    @PutMapping("/activate-staff/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> activateStaffById(@PathVariable int id, @RequestBody ActiveStaffRequest request) {
        log.info("Received request to activate staff with ID: {} and note: {}", id, request.note());
        ResponseData<?> result = staffService.activateStaffById(id, request);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff activated successfully with ID: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.getStatus() == ResponseCode.C204.getCode()) {
            log.warn("Staff already active with ID: {}", id);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        log.error("Failed to activate staff with ID: {}", id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Create university response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/create-university/{id}")
    public ResponseEntity<ResponseData<CreateUniversityRequestDTO>> createUniversity(@PathVariable int id) {
        return ResponseEntity.ok(ResponseData.ok("Lấy thông tin tạo trường đại học thành công", createUniversityService.getById(id)));
    }

    /**
     * Accept create university request response entity.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     * @throws StoreDataFailedException  the store data failed exception
     */
    @PutMapping("/create-university/accept/{id}")
    public ResponseEntity<?> acceptCreateUniversityRequest(@PathVariable("id") Integer id, @RequestBody CreateUniversityRequestAdminActionRequest request)
            throws ResourceNotFoundException, StoreDataFailedException {
        return ResponseEntity.ok(createUniversityService.adminAction(id, CreateUniversityRequestStatus.ACCEPTED, request.adminNote()));
    }

    /**
     * Reject create university request response entity.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     * @throws StoreDataFailedException  the store data failed exception
     */
    @PutMapping("/create-university/reject/{id}")
    public ResponseEntity<?> rejectCreateUniversityRequest(@PathVariable("id") Integer id, @RequestBody CreateUniversityRequestAdminActionRequest request)
            throws ResourceNotFoundException, StoreDataFailedException {
        return ResponseEntity.ok(createUniversityService.adminAction(id, CreateUniversityRequestStatus.REJECTED, request.adminNote()));
    }

    /**
     * Active university by id response entity.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     * @throws StoreDataFailedException  the store data failed exception
     */
    @PutMapping("/university/change-status/{id}")
    public ResponseEntity<ResponseData> activeUniversityById(@PathVariable Integer id, @RequestBody UpdateUniversityStatusRequest request) throws ResourceNotFoundException, StoreDataFailedException {
        return ResponseEntity.ok(universityService.updateUniversityStatus(id, request.note(), request.status()));
    }

    /**
     * Gets create university requests.
     *
     * @param pageable           the pageable
     * @param id                 the id
     * @param universityName     the university name
     * @param universityCode     the university code
     * @param universityEmail    the university email
     * @param universityUsername the university username
     * @param status             the status
     * @param createBy           the create by
     * @param createByName       the create by name
     * @param confirmBy          the confirm by
     * @return the create university requests
     */
    @GetMapping("/create-university-request")
    public ResponseEntity<ResponseData<Page<CreateUniversityRequestDTO>>> getCreateUniversityRequests(
            Pageable pageable,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String universityName,
            @RequestParam(required = false) String universityCode,
            @RequestParam(required = false) String universityEmail,
            @RequestParam(required = false) String universityUsername,
            @RequestParam(required = false) List<CreateUniversityRequestStatus> status,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) String createByName,
            @RequestParam(required = false) Integer confirmBy
    ) {
        return ResponseEntity.ok(
                createUniversityService.getBy(
                        pageable, id, universityName, universityCode, universityEmail,
                        universityUsername, status, createBy, createByName, confirmBy
                ));
    }

    /**
     * Gets university management.
     *
     * @param pageable     the pageable
     * @param id           the id
     * @param code         the code
     * @param username     the username
     * @param name         the name
     * @param phone        the phone
     * @param email        the email
     * @param status       the status
     * @param createBy     the create by
     * @param createByName the create by name
     * @return the university management
     */
    @GetMapping("/university")
    public ResponseEntity<ResponseData<Page<UniversityFullResponseDTO>>> getUniversityManagement(
            Pageable pageable,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) AccountStatus status,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) String createByName) {
        return ResponseEntity.ok(
                universityService.getAllUniversityFullResponses(pageable,
                        id, code, username, name, phone,
                        email, status, createBy, createByName));
    }


    /**
     * Find full university by id response entity.
     *
     * @param id the id
     * @return the response entity
     * @throws Exception the exception
     */
    @GetMapping("/university/{id}")
    public ResponseEntity<ResponseData<UniversityFullResponseDTO>> findFullUniversityById(@PathVariable Integer id) throws Exception {
        var result = ResponseData.ok("Lấy thông tin trường thành công", universityService.getUniversityFullResponseById(id));
        return ResponseEntity.ok(result);
    }

    /**
     * Gets list package history.
     *
     * @param adsName  the ads name
     * @param status   the status
     * @param pageable the pageable
     * @return the list package history
     */
    @GetMapping("/package/list")
    public ResponseEntity<ResponseData<Page<PackageResponseDTO>>> getListPackageHistory(@RequestParam(name = "adsName", required = false) String adsName,
                                                                                        @RequestParam(name = "status", required = false) String status,
                                                                                        @RequestParam(name = "orderCode", required = false) String orderCode,
                                                                                        Pageable pageable) {
        ResponseData<Page<PackageResponseDTO>> result = universityTransactionService.getListPackage(adsName, status, orderCode, pageable);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

}