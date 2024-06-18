package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.university.CreateUniversityTicketRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.CreateUniversityTicketResponseDTO;
import com.demo.admissionportal.service.CreateUniversityTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller for managing university ticket creation operations.
 *
 * @author hopeless
 * @version 1.0
 * @since 19/06/2024
 */
@RestController
@RequestMapping("/api/v1/create-university-ticket")
@RequiredArgsConstructor
public class CreateUniversityTicketController {

    private final CreateUniversityTicketService createUniversityTicketService;

    /**
     * Creates a new university ticket.
     *
     * @param request The request containing university ticket information ({@link CreateUniversityTicketRequestDTO}).
     * @return A response entity containing the created university ticket information or an error message. The response data object will be of type {@link ResponseData} with the payload being a {@link CreateUniversityTicketResponseDTO} object on success, or an error message otherwise.
     */
    @PostMapping
    public ResponseEntity<ResponseData<CreateUniversityTicketResponseDTO>> createTicket(
            @RequestBody @Valid CreateUniversityTicketRequestDTO request) {

        ResponseData<CreateUniversityTicketResponseDTO> response = createUniversityTicketService.createCreateUniversityTicket(request);

        // Handle internal server error case (optional logic improvement)
        if (response.getStatus() == ResponseCode.C201.getCode()) {
            // Consider logging the error or providing a more informative message
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok().body(response);
    }
}

