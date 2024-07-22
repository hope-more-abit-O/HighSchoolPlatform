package com.demo.admissionportal.dto.entity.consultant;

import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import lombok.*;

/**
 * ConsultantInfoResponseDTO is a data transfer object that encapsulates the response data
 * for a consultant, including user account information and consultant details.
 *
 * <p>This class contains two main properties:
 * <ul>
 *   <li>{@link InfoUserResponseDTO} account - Information about the user's account</li>
 *   <li>{@link ConsultantInfoResponseDTO} info - Detailed information about the consultant</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * InfoUserResponseDTO userAccount = new InfoUserResponseDTO(...);
 * InfoConsultantResponseDTO consultantInfo = new InfoConsultantResponseDTO(...);
 * ConsultantInfoResponseDTO consultantResponse = new ConsultantInfoResponseDTO();
 * consultantResponse.setAccount(userAccount);
 * consultantResponse.setInfo(consultantInfo);
 * }
 * </pre>
 * </p>
 *
 * @see InfoUserResponseDTO
 * @see ConsultantInfoResponseDTO
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoConsultantResponseDTO {
    private InfoUserResponseDTO account;
    private ConsultantInfoResponseDTO info;
}
