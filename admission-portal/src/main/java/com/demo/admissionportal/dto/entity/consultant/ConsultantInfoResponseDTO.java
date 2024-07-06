package com.demo.admissionportal.dto.entity.consultant;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.dto.entity.university.UniversityInfoResponseDTO;
import lombok.*;

/**
 * InfoConsultantResponseDTO is a data transfer object that encapsulates the response data
 * for a consultant, including details about the university they are associated with,
 * their personal information, and additional notes.
 *
 * <p>This class contains the following properties:
 * <ul>
 *   <li>{@link UniversityInfoResponseDTO} university - Information about the associated university</li>
 *   <li>{@link String} name - The name of the consultant</li>
 *   <li>{@link String} phone - The phone number of the consultant</li>
 *   <li>{@link String} address - The address of the consultant</li>
 *   <li>{@link String} note - Any additional notes about the consultant</li>
 *   <li>{@link Gender} gender - The gender of the consultant</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * InfoConsultantResponseDTO consultantResponse = InfoConsultantResponseDTO.builder()
 *     .university(new UniversityInfoResponseDTO(...))
 *     .name("John Doe")
 *     .phone("123-456-7890")
 *     .address("123 Main St, Anytown, USA")
 *     .note("Experienced in international student admissions")
 *     .gender(Gender.MALE)
 *     .build();
 * }
 * </pre>
 * </p>
 *
 * @see UniversityInfoResponseDTO
 * @see Gender
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantInfoResponseDTO {
    private UniversityInfoResponseDTO university;
    private String name;
    private String phone;
    private String address;
    private String note;
    private Gender gender;
}
