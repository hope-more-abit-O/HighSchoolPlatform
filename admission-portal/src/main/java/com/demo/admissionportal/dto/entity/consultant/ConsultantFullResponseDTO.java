package com.demo.admissionportal.dto.entity.consultant;

import com.demo.admissionportal.dto.entity.user.UserResponseDTO;
import lombok.*;

/**
 * ConsultantFullResponseDTO is a data transfer object that encapsulates the full response data
 * for a consultant, including user account information and detailed consultant information.
 *
 * <p>This class contains two main properties:
 * <ul>
 *   <li>{@link UserResponseDTO} account - Information about the user's account</li>
 *   <li>{@link ConsultantResponseDTO} info - Detailed information about the consultant</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * UserResponseDTO userAccount = new UserResponseDTO(...);
 * ConsultantResponseDTO consultantInfo = new ConsultantResponseDTO(...);
 * ConsultantFullResponseDTO consultantResponse = new ConsultantFullResponseDTO();
 * consultantResponse.setAccount(userAccount);
 * consultantResponse.setInfo(consultantInfo);
 * }
 * </pre>
 * </p>
 *
 * @see UserResponseDTO
 * @see ConsultantResponseDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantFullResponseDTO {
    private UserResponseDTO account;
    private ConsultantResponseDTO info;
}
