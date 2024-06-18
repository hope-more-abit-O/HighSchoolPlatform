package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.university.CreateUniversityTicketRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.CreateUniversityTicketResponseDTO;
/**
 * Service interface for creating university tickets.
 *
 * This interface defines a contract for methods that create university tickets. Implementations of this interface will provide the specific logic for creating tickets.
 */
public interface CreateUniversityTicketService {

    /**
     * Creates a new university ticket.
     *
     * This method takes a {@link CreateUniversityTicketRequestDTO} object as input, processes it, and creates a corresponding university ticket entity. The created ticket information is then returned as a {@link ResponseData} object containing a {@link CreateUniversityTicketResponseDTO} on success, or an error message on failure.
     *
     * @param request The request DTO containing university ticket information.
     * @return A response data object indicating success or failure of the ticket creation. On success, it includes the created ticket information as a {@link CreateUniversityTicketResponseDTO} object.
     */
    public ResponseData<CreateUniversityTicketResponseDTO> createCreateUniversityTicket(CreateUniversityTicketRequestDTO request);
}