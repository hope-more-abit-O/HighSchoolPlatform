package com.demo.admissionportal.dto.entity.consultant;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.dto.entity.address.InfoDistrictDTO;
import com.demo.admissionportal.dto.entity.address.InfoProvinceDTO;
import com.demo.admissionportal.dto.entity.address.InfoWardDTO;
import com.demo.admissionportal.dto.entity.university.UniversityInfoResponseDTO;
import com.demo.admissionportal.entity.ConsultantInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ConsultantResponseDTO is a data transfer object that encapsulates the response data
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
 * ConsultantResponseDTO consultantResponse = new ConsultantResponseDTO();
 * consultantResponse.convertName(consultantInfo);
 * }
 * </pre>
 * </p>
 *
 * @see UniversityInfoResponseDTO
 * @see Gender
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantResponseDTO {
    private String name;
    private String phone;
    private String specificAddress;
    private InfoProvinceDTO province;
    private InfoDistrictDTO district;
    private InfoWardDTO ward;
    private String address;
    private String note;
    private String gender;

    /**
     * Converts the individual name components of the given {@link ConsultantInfo} into a full name
     * and sets it to the {@code name} property of this {@code InfoConsultantResponseDTO}.
     *
     * <p>The full name is constructed by concatenating the first name, middle name (if present), and last name,
     * with each component separated by a space. Leading and trailing spaces are trimmed from the final name.</p>
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * ConsultantInfo consultantInfo = new ConsultantInfo();
     * consultantInfo.setFirstname("John");
     * consultantInfo.setMiddleName("Michael");
     * consultantInfo.setLastName("Doe");
     *
     * InfoConsultantResponseDTO consultantResponse = new InfoConsultantResponseDTO();
     * consultantResponse.convertName(consultantInfo);
     *
     * // The name property of consultantResponse will be set to "John Michael Doe"
     * }
     * </pre>
     * </p>
     *
     * @param consultantInfo the {@code ConsultantInfo} object containing the name components to be converted
     * @see ConsultantInfo
     */
    public void convertName(ConsultantInfo consultantInfo){
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(consultantInfo.getFirstName()).append(" ");
        if (consultantInfo.getMiddleName() != null && !consultantInfo.getMiddleName().isEmpty())
            nameBuilder.append(consultantInfo.getMiddleName()).append(" ");
        nameBuilder.append(consultantInfo.getLastName()).append(" ");

        this.setName(nameBuilder.toString().trim());
    }
}
