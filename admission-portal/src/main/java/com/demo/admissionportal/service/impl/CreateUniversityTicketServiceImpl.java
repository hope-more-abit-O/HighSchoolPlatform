package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.request.university.CreateUniversityTicketRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.CreateUniversityTicketResponseDTO;
import com.demo.admissionportal.entity.CreateUniversityTicket;
import com.demo.admissionportal.repository.CreateUniversityTicketRepository;
import com.demo.admissionportal.service.CreateUniversityTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Service implementation for creating university tickets.
 *
 * This class implements the {@link CreateUniversityTicketService} interface and provides methods for creating university tickets.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CreateUniversityTicketServiceImpl implements CreateUniversityTicketService {
    private final CreateUniversityTicketRepository createUniversityTicketRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new university ticket.
     *
     * This method takes a {@link CreateUniversityTicketRequestDTO} object as input, creates a corresponding {@link CreateUniversityTicket} entity, saves it to the database using the injected repository, and returns a {@link ResponseData} object containing the result.
     *
     * @param request The request DTO containing university ticket information.
     * @return A response data object indicating success or failure of the ticket creation. On success, it includes the created ticket information as a {@link CreateUniversityTicketResponseDTO} object.
     */
    @Override
    public ResponseData<CreateUniversityTicketResponseDTO> createCreateUniversityTicket(CreateUniversityTicketRequestDTO request) {
        log.info("Creating CreateUniversityTicket.");
        CreateUniversityTicket createUniversityTicket = createUniversityTicketRepository
                .save(new CreateUniversityTicket(request));
        if (createUniversityTicket == null) {
            log.warn("Create CreateUniversityTicket failed.");
            return ResponseData.error("Tạo Ticket thất bại!");
        }

        log.info("Create CreateUniversityTicket successes.");
        return ResponseData.ok("Tạo Ticket thành công.", CreateUniversityTicketResponseDTO.toDTO(createUniversityTicket));
    }
}
