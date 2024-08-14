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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/university")
@RequiredArgsConstructor
public class UniversityController {
    private final UniversityService universityService;
    private final ConsultantService consultantService;
    private final UniversityTransactionService universityTransactionService;

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
     * @param request The consultant creation request data, expected
     *                as a JSON object in the request body.
     * @return A ResponseEntity containing the result of the operation
     * with a suitable HTTP status code (e.g., 201 Created for success).
     * @throws DataExistedException If the provided data conflicts
     *                              with existing records.
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
     * @return A ResponseEntity containing consultant details with
     * a suitable HTTP status (e.g., 200 OK for success, 404 Not Found
     * if the consultant is not found).
     */
    @GetMapping("/consultant/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> getById(@PathVariable Integer id) throws NotAllowedException, ResourceNotFoundException {
        return ResponseEntity.ok(ResponseData.ok("Lấy thông tin tư vấn viên thành công.", consultantService.getFullConsultantByIdByUniversity(id)));
    }

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

    @GetMapping("/info")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<UniversityFullResponseDTO>> getSelfProfile() {
        return ResponseEntity.ok(ResponseData.ok("Lấy thông tin trường thành công.", universityService.getSelfProfile()));
    }

    @GetMapping("/full/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<UniversityFullResponseDTO>> findFullUniversityById(@PathVariable Integer id) throws Exception {
        var result = ResponseData.ok("Lấy thông tin trường thành công", universityService.getUniversityFullResponseById(id));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<ResponseData<UniversityInfoResponseDTO>> findInfoUniversityById(@PathVariable Integer id) throws Exception {
        var result = ResponseData.ok("Lấy thông tin trường thành công", universityService.getUniversityInfoResponseById(id));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/consultant")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData> updateConsultantStatus(@RequestBody @Valid PatchConsultantStatusRequest request) {
        return ResponseEntity.ok(ResponseData.ok("Cập nhật trạng thái tư vấn viên thành công.", consultantService.updateConsultantStatus(request)));
    }


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

    @GetMapping("/transaction")
    public ResponseEntity<ResponseData<List<OrderResponseDTO>>> getOrderByUniID() {
        return ResponseEntity.ok(universityTransactionService.getOrderByUniId());
    }
}
