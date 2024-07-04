package com.demo.admissionportal.dto.entity.university;

import com.demo.admissionportal.dto.entity.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UniversityFullResponseDTO is a data transfer object that encapsulates the response data
 * for a university, including user account information and university details.
 *
 * <p>This class contains two main properties:
 * <ul>
 *   <li>{@link UserResponseDTO} account - Information about the user's account</li>
 *   <li>{@link UniversityResponseDTO} info - Information about the university</li>
 * </ul>
 * </p>
 *
 * <p>It provides constructors for creating an instance with or without initial data,
 * and getter and setter methods to access and modify the properties.</p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * UserResponseDTO userAccount = new UserResponseDTO(...);
 * UniversityResponseDTO universityInfo = new UniversityResponseDTO(...);
 * UniversityFullResponseDTO response = new UniversityFullResponseDTO(userAccount, universityInfo);
 * }
 * </pre>
 * </p>
 *
 * @see UserResponseDTO
 * @see UniversityResponseDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityFullResponseDTO {
    private UserResponseDTO account;
    private UniversityResponseDTO info;
}
