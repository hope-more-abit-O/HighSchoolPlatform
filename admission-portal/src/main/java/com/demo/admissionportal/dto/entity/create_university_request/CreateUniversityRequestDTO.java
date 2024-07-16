package com.demo.admissionportal.dto.entity.create_university_request;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * CreateUniversityRequestDTO is a data transfer object that encapsulates the request data
 * for creating a university, including various details about the university and the actioners
 * who created and updated the request.
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
 *   <li>{@link String} createTime - The timestamp of when the request was created</li>
 *   <li>{@link ActionerDTO} createBy - The actioner who created the request</li>
 *   <li>{@link String} updateTime - The timestamp of when the request was last updated</li>
 *   <li>{@link ActionerDTO} updateBy - The actioner who last updated the request</li>
 *   <li>{@link String} status - The current status of the request</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * CreateUniversityRequestDTO request = new CreateUniversityRequestDTO();
 * request.setId(1);
 * request.setUniversityName("Example University");
 * request.setUniversityCode("EX123");
 * request.setUniversityEmail("info@example.com");
 * request.setUniversityUsername("example_univ");
 * request.setUniversityType("Public");
 * request.setNote("Initial request");
 * request.setDocuments(Arrays.asList("doc1.pdf", "doc2.pdf"));
 * request.setCreateTime("2024-07-04T10:00:00Z");
 * request.setCreateBy(new ActionerDTO(...));
 * request.setUpdateTime("2024-07-05T12:00:00Z");
 * request.setUpdateBy(new ActionerDTO(...));
 * request.setStatus("Pending");
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
public class CreateUniversityRequestDTO {
    private Integer id;
    private String universityName;
    private String universityCode;
    private String universityEmail;
    private String universityUsername;
    private String universityType;
    private String note;
    private List<String> documents;
    private String createTime;
    private ActionerDTO createBy;
    private String updateTime;
    private ActionerDTO updateBy;
    private ActionerDTO confirmBy;
    private String status;
}
