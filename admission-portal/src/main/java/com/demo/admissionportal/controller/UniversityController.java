package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.entity.university.UniversityInfoResponseDTO;
import com.demo.admissionportal.dto.request.consultant.CreateConsultantRequest;
import com.demo.admissionportal.dto.request.consultant.PatchConsultantStatusRequest;
import com.demo.admissionportal.dto.request.university.UpdateUniversityInfoRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.payment.OrderResponseDTO;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.exception.exceptions.NotAllowedException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.service.ConsultantService;
import com.demo.admissionportal.service.UniversityService;
import com.demo.admissionportal.service.UniversityTransactionService;
import com.demo.admissionportal.util.impl.ServiceUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type University controller.
 */
@RestController
@RequestMapping("/api/v1/university")
@RequiredArgsConstructor
public class UniversityController {
    private final UniversityService universityService;
    private final ConsultantService consultantService;
    private final UniversityTransactionService universityTransactionService;

    /**
     * Update info response entity.
     *
     * @param updateUniversityInfoRequest the update university info request
     * @return the response entity
     */
    @PutMapping("/info")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity updateInfo(@RequestBody @Valid UpdateUniversityInfoRequest updateUniversityInfoRequest) {
        try {
            return ResponseEntity.ok(universityService.updateUniversityInfo(updateUniversityInfoRequest));
        } catch (ResourceNotFoundException | StoreDataFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new consultant.
     *
     * <p>Receives a consultant creation request, delegates
     * the creation logic to the service layer, and returns a
     * response indicating success or failure.
     *
     * @param request The consultant creation request data, expected                as a JSON object in the request body.
     * @return A ResponseEntity containing the result of the operation with a suitable HTTP status code (e.g., 201 Created for success).
     * @throws DataExistedException      If the provided data conflicts                              with existing records.
     * @throws StoreDataFailedException  the store data failed exception
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PostMapping("/consultant")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData> createConsultant(@RequestBody @Valid CreateConsultantRequest request)
            throws DataExistedException, StoreDataFailedException, ResourceNotFoundException {
        return ResponseEntity.ok(consultantService.createConsultant(request));
    }

    /**
     * Retrieves details of a consultant by their ID.
     *
     * <p>Fetches detailed information about a consultant using
     * their unique identifier and returns the data as a response.
     *
     * @param id The unique identifier of the consultant to retrieve.
     * @return A ResponseEntity containing consultant details with a suitable HTTP status (e.g., 200 OK for success, 404 Not Found if the consultant is not found).
     * @throws NotAllowedException       the not allowed exception
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/consultant/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> getById(@PathVariable Integer id) throws NotAllowedException, ResourceNotFoundException {
        return ResponseEntity.ok(ResponseData.ok("Lấy thông tin tư vấn viên thành công.", consultantService.getFullConsultantByIdByUniversity(id)));
    }

    /**
     * Gets consultants.
     *
     * @param pageable       the pageable
     * @param id             the id
     * @param name           the name
     * @param universityName the university name
     * @param universityId   the university id
     * @param username       the username
     * @param status         the status
     * @param updateBy       the update by
     * @return the consultants
     * @throws NotAllowedException       the not allowed exception
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/consultants")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData> getConsultants(
            Pageable pageable,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String universityName,
            @RequestParam(required = false) Integer universityId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) List<AccountStatus> status,
            @RequestParam(required = false) Integer updateBy
    ) throws NotAllowedException, ResourceNotFoundException {
        Integer uniId = ServiceUtils.getId();
        return ResponseEntity.ok(
                ResponseData.ok("Tìm mọi tư vấn viên dưới quyền thành công.",
                        consultantService.getFullConsultants(
                                pageable, id, name, username, universityName, universityId, status, uniId, updateBy
                        )
                )
        );
    }

    /**
     * Gets self profile.
     *
     * @return the self profile
     */
    @GetMapping("/info")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<UniversityFullResponseDTO>> getSelfProfile() {
        return ResponseEntity.ok(ResponseData.ok("Lấy thông tin trường thành công.", universityService.getSelfProfile()));
    }

    /**
     * Find full university by id response entity.
     *
     * @param id the id
     * @return the response entity
     * @throws Exception the exception
     */
    @GetMapping("/full/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<UniversityFullResponseDTO>> findFullUniversityById(@PathVariable Integer id) throws Exception {
        var result = ResponseData.ok("Lấy thông tin trường thành công", universityService.getUniversityFullResponseById(id));
        return ResponseEntity.ok(result);
    }

    /**
     * Find info university by id response entity.
     *
     * @param id the id
     * @return the response entity
     * @throws Exception the exception
     */
    @GetMapping("/info/{id}")
    public ResponseEntity<ResponseData<UniversityInfoResponseDTO>> findInfoUniversityById(@PathVariable Integer id) throws Exception {
        var result = ResponseData.ok("Lấy thông tin trường thành công", universityService.getUniversityInfoResponseById(id));
        return ResponseEntity.ok(result);
    }

    /**
     * Update consultant status response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PutMapping("/consultant")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData> updateConsultantStatus(@RequestBody @Valid PatchConsultantStatusRequest request) {
        return ResponseEntity.ok(ResponseData.ok("Cập nhật trạng thái tư vấn viên thành công.", consultantService.updateConsultantStatus(request)));
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
    @GetMapping()
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
     * Gets order by uni id.
     *
     * @param orderCode the order code
     * @param status    the status
     * @param pageable  the pageable
     * @return the order by uni id
     */
    @GetMapping("/transaction")
    @PreAuthorize("hasAnyAuthority('UNIVERSITY','CONSULTANT')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<Page<OrderResponseDTO>>> getOrderByUniID(@RequestParam(name = "orderCode", required = false) String orderCode,
                                                                                @RequestParam(name = "status", required = false) String status,
                                                                                Pageable pageable) {
        return ResponseEntity.ok(universityTransactionService.getOrderByUniId(orderCode, status, pageable));
    }

}
