package com.demo.admissionportal.dto.entity.university;

import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UniversityInfoResponseDTO is a data transfer object that encapsulates the response data
 * for a university, including user account information and university details.
 *
 * <p>This class contains two main properties:
 * <ul>
 *   <li>{@link InfoUserResponseDTO} account - Information about the user's account</li>
 *   <li>{@link InfoUniversityResponseDTO} info - Information about the university</li>
 * </ul>
 * </p>
 *
 * <p>It provides constructors for creating an instance with or without initial data,
 * and getter and setter methods to access and modify the properties.</p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * InfoUserResponseDTO userAccount = new InfoUserResponseDTO(...);
 * InfoUniversityResponseDTO universityInfo = new InfoUniversityResponseDTO(...);
 * UniversityInfoResponseDTO response = new UniversityInfoResponseDTO(userAccount, universityInfo);
 * }
 * </pre>
 * </p>
 *
 * @see InfoUserResponseDTO
 * @see InfoUniversityResponseDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversityInfoResponseDTO {
    private InfoUserResponseDTO account;
    private InfoUniversityResponseDTO info;
}
