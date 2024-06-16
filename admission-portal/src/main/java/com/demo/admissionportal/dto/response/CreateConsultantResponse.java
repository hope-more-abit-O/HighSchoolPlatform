package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.dto.response.entity.ConsultantResponseDTO;
import com.demo.admissionportal.dto.response.entity.sub_entity.UniversityConsultantResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO representing the response for creating a new consultant and their association with a university.
 *
 * @author hopeless
 * @version 1.0
 * @since 16/06/2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateConsultantResponse {

    /**
     * The consultant information.
     */
    private ConsultantResponseDTO consultant;

    /**
     * The university-consultant association information.
     */
    private UniversityConsultantResponseDTO universityConsultant;
}