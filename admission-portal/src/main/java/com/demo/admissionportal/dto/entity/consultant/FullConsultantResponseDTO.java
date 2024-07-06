package com.demo.admissionportal.dto.entity.consultant;

import com.demo.admissionportal.dto.entity.user.UserResponseDTOV2;
import lombok.*;

/**
 * ConsultantFullResponseDTO is a data transfer object that encapsulates the full response data
 * for a consultant, including user account information and detailed consultant information.
 *
 * <p>This class contains two main properties:
 * <ul>
 *   <li>{@link UserResponseDTOV2} account - Information about the user's account</li>
 *   <li>{@link ConsultantResponseDTO} info - Detailed information about the consultant</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * UserResponseDTOV2 userAccount = new UserResponseDTOV2(...);
 * ConsultantResponseDTO consultantInfo = new ConsultantResponseDTO(...);
 * ConsultantFullResponseDTO consultantResponse = new ConsultantFullResponseDTO();
 * consultantResponse.setAccount(userAccount);
 * consultantResponse.setInfo(consultantInfo);
 * }
 * </pre>
 * </p>
 *
 * @see UserResponseDTOV2
 * @see ConsultantResponseDTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullConsultantResponseDTO {
    private UserResponseDTOV2 account;
    private ConsultantResponseDTO info;
}
