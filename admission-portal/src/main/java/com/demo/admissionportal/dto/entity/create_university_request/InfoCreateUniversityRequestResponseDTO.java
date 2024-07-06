package com.demo.admissionportal.dto.entity.create_university_request;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * InfoCreateUniversityRequestResponseDTO is a data transfer object that encapsulates the response data
 * for a request to create a university, including various details about the university and the actioner who confirmed it.
 *
 * <p>This class contains the following properties:
 * <ul>
 *   <li>{@link Integer} id - The unique identifier of the request</li>
 *   <li>{@link String} universityName - The name of the university</li>
 *   <li>{@link String} universityCode - The code of the university</li>
 *   <li>{@link String} universityEmail - The email address of the university</li>
 *   <li>{@link String} universityUsername - The username of the university</li>
 *   <li>{@link String} universityType - The type of the university</li>
 *   <li>{@link String} note - Any additional notes regarding the request</li>
 *   <li>{@link String} status - The current status of the request</li>
 *   <li>{@link ActionerDTO} confirmBy - The actioner who confirmed the request</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * InfoCreateUniversityRequestResponseDTO requestResponse = new InfoCreateUniversityRequestResponseDTO();
 * requestResponse.setId(1);
 * requestResponse.setUniversityName("Example University");
 * requestResponse.setUniversityCode("EX123");
 * requestResponse.setUniversityEmail("info@example.com");
 * requestResponse.setUniversityUsername("example_univ");
 * requestResponse.setUniversityType("Public");
 * requestResponse.setNote("Initial request");
 * requestResponse.setStatus("Pending");
 * requestResponse.setConfirmBy(new ActionerDTO(...));
 * }
 * </pre>
 * </p>
 *
 * @see ActionerDTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfoCreateUniversityRequestResponseDTO {
    private Integer id;
    private String universityName;
    private String universityCode;
    private String universityEmail;
    private String universityUsername;
    private String universityType;
    private String note;
    private String status;
    private ActionerDTO confirmBy;
}
