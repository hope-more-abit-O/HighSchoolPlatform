package com.demo.admissionportal.dto.entity.create_university_request;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * CreateUniversityRequestResponseDTO is a data transfer object that encapsulates the response data
 * for a request to create a university, including details about the university, the actioners involved,
 * and the request's status.
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
 *   <li>{@link List<String>} documents - A list of documents related to the request</li>
 *   <li>{@link ActionerDTO} createBy - The actioner who created the request</li>
 *   <li>{@link String} createTime - The timestamp of when the request was created</li>
 *   <li>{@link ActionerDTO} updateBy - The actioner who last updated the request</li>
 *   <li>{@link String} updateTime - The timestamp of when the request was last updated</li>
 *   <li>{@link String} status - The current status of the request</li>
 *   <li>{@link ActionerDTO} confirmBy - The actioner who confirmed the request</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * CreateUniversityRequestResponseDTO response = new CreateUniversityRequestResponseDTO();
 * response.setId(1);
 * response.setUniversityName("Example University");
 * response.setUniversityCode("EX123");
 * response.setUniversityEmail("info@example.com");
 * response.setUniversityUsername("example_univ");
 * response.setUniversityType("Public");
 * response.setNote("Request approved");
 * response.setDocuments(Arrays.asList("doc1.pdf", "doc2.pdf"));
 * response.setCreateBy(new ActionerDTO(...));
 * response.setCreateTime("2024-07-04T10:00:00Z");
 * response.setUpdateBy(new ActionerDTO(...));
 * response.setUpdateTime("2024-07-05T12:00:00Z");
 * response.setStatus("Approved");
 * response.setConfirmBy(new ActionerDTO(...));
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
public class CreateUniversityRequestFullResponseDTO {
    private Integer id;
    private String universityName;
    private String universityCode;
    private String universityEmail;
    private String universityUsername;
    private String universityType;
    private String note;
    private List<String> documents;
    private ActionerDTO createBy;
    private String createTime;
    private ActionerDTO updateBy;
    private String updateTime;
    private String status;
    private ActionerDTO confirmBy;
}
