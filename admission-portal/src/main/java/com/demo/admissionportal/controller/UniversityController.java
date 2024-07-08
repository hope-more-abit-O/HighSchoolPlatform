package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.consultant.CreateConsultantRequest;
import com.demo.admissionportal.dto.response.consultant.ChangeConsultantStatusRequest;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.NotAllowedException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.service.ConsultantService;
import com.demo.admissionportal.service.UniversityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/university")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasAuthority('UNIVERSITY')")
public class UniversityController {
    private final UniversityService universityService;
    private final ConsultantService consultantService;

    /**
     * Creates a new consultant.
     *
     * <p>Receives a consultant creation request, delegates
     * the creation logic to the service layer, and returns a
     * response indicating success or failure.
     *
     * @param request The consultant creation request data, expected
     *               as a JSON object in the request body.
     * @return  A ResponseEntity containing the result of the operation
     *          with a suitable HTTP status code (e.g., 201 Created for success).
     * @throws DataExistedException  If the provided data conflicts
     *                                 with existing records.
     */
    @PostMapping
    public ResponseEntity<ResponseData> createConsultant(@RequestBody @Valid CreateConsultantRequest request)
            throws DataExistedException {
        return ResponseEntity.ok(consultantService.createConsultant(request));
    }

    @PatchMapping("/consultant/change-status/{id}")
    public ResponseEntity<?> changeConsultantStatus(@PathVariable Integer id, @RequestBody ChangeConsultantStatusRequest request)
            throws BadRequestException, StoreDataFailedException, DataExistedException, NotAllowedException {
        return ResponseEntity.ok(consultantService.updateConsultantStatus(id, request));
    }

    @GetMapping("/info")
    public ResponseEntity<UniversityFullResponseDTO> getUniversityInfo() {
        return ResponseEntity.ok(universityService.getUniversityFullResponse());
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ResponseData<UniversityFullResponseDTO>> findFullUniversityById(@PathVariable Integer id) throws Exception {
        var result = ResponseData.ok("hi",universityService.getUniversityFullResponseById(id));
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves details of a consultant by their ID.
     *
     * <p>Fetches detailed information about a consultant using
     * their unique identifier and returns the data as a response.
     *
     * @param id  The unique identifier of the consultant to retrieve.
     * @return     A ResponseEntity containing consultant details with
     *             a suitable HTTP status (e.g., 200 OK for success, 404 Not Found
     *             if the consultant is not found).
     */
    @GetMapping("/consultant/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(consultantService.getFullConsultantById(id));
    }
}
