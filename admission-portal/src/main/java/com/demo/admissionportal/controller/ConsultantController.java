package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.CreateConsultantRequest;
import com.demo.admissionportal.dto.request.consultant.SelfUpdateConsultantInfoRequest;
import com.demo.admissionportal.dto.request.consultant.UpdateConsultantInfoByIdRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.consultant.ChangeConsultantStatusRequest;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.service.ConsultantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling endpoints related to consultant management.
 */
@RestController
@RequestMapping("/api/v1/consultant")
@RequiredArgsConstructor
public class ConsultantController {
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(consultantService.getFullConsultantById(id));
    }

    //TODO: JAVADOC
    @PutMapping("/update")
    public ResponseEntity<?> seftUpdateConsultantInfo(@RequestBody SelfUpdateConsultantInfoRequest request){
        return ResponseEntity.ok(consultantService.selfUpdateConsultantInfo(request));
    }


    @PutMapping("/update-for")
    public ResponseEntity<?> updateConsultantInfo(@RequestBody UpdateConsultantInfoByIdRequest request){
        return ResponseEntity.ok(consultantService.updateConsultantInfoById(request));
    }

    @PatchMapping("/change-status/{id}")
    public ResponseEntity<?> changeConsultantStatus(@PathVariable Integer id, @RequestBody ChangeConsultantStatusRequest request) throws BadRequestException, StoreDataFailedException, DataExistedException {
        return ResponseEntity.ok(consultantService.updateConsultantStatus(id, request));
    }
}
