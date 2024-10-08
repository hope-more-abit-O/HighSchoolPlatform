package com.demo.admissionportal.dto.entity.university;

import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusDTO;
import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusProperties;
import com.demo.admissionportal.dto.entity.user.FullUserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * UniversityFullResponseDTO is a data transfer object that encapsulates the response data
 * for a university, including user account information and university details.
 *
 * <p>This class contains two main properties:
 * <ul>
 *   <li>{@link FullUserResponseDTO} account - Information about the user's account</li>
 *   <li>{@link FullUniversityResponseDTO} info - Information about the university</li>
 * </ul>
 * </p>
 *
 * <p>It provides constructors for creating an instance with or without initial data,
 * and getter and setter methods to access and modify the properties.</p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * UserResponseDTOV2 userAccount = new UserResponseDTOV2(...);
 * UniversityResponseDTO universityInfo = new UniversityResponseDTO(...);
 * UniversityFullResponseDTO response = new UniversityFullResponseDTO(userAccount, universityInfo);
 * }
 * </pre>
 * </p>
 *
 * @see FullUserResponseDTO
 * @see FullUniversityResponseDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityFullResponseDTO {
    private FullUserResponseDTO account;
    private FullUniversityResponseDTO info;
    private List<UniversityCampusProperties> campuses;
}
