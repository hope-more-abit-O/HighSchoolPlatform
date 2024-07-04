package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.entity.university.UniversityInfoResponseDTO;
import com.demo.admissionportal.dto.entity.university.UniversityResponseDTO;
import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import com.demo.admissionportal.dto.entity.user.UserResponseDTOV2;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.service.impl.UniversityServiceImpl;

/**
 * Provides methods for managing and retrieving university-related information.
 *
 * <p>This interface defines the contract for interacting with university
 * data, encompassing functionalities like retrieving university details and managing
 * related information.
 *
 * @see UniversityServiceImpl - A typical implementation of this interface.
 */
public interface UniversityService {
    /**
     * Retrieves detailed information about a university, combining account
     * data with university-specific information.
     *
     * <p> This method fetches both user account details (using the `UserService`)
     * and university-specific information from their respective sources and
     * combines them into a {@link UniversityFullResponseDTO} for a comprehensive view.
     *
     * <p> Example Usage:
     * <pre>
     * {@code
     * try {
     *     Integer universityId = 1; // Replace with the actual university ID
     *
     *     UniversityFullResponseDTO universityFullInfo = universityService.getUniversityFullResponseById(universityId);
     *
     *     // ... access and process other data from the universityFullInfo object
     *
     * } catch (ResourceNotFoundException e) {
     *     // Handle the case where a university with the given ID is not found.
     *     //  - Log the error (as it's already logged within the method).
     *     //  - Return an appropriate error response to the client.
     * }
     * }
     * </pre>
     *
     * @param id The unique ID of the university to retrieve.
     * @return A {@link UniversityFullResponseDTO} object populated with account
     *         and information details for the specified university.
     * @throws ResourceNotFoundException If no university is found matching the given ID.
     *
     * @see UniversityFullResponseDTO
     * @see UserResponseDTOV2
     * @see UniversityResponseDTO
     */
    public UniversityFullResponseDTO getUniversityFullResponseById(Integer id);
    /**
     * Retrieves a university's information, combining both user and
     * university-specific data.
     *
     * <p> This method fetches user-related information (using the `UserService`) and
     * university-specific data from their respective sources. It then combines
     * this data into a {@link UniversityInfoResponseDTO} object.
     *
     * <p> Example Usage:
     * <pre>
     * {@code
     *  try {
     *      Integer universityId = 1234; // Replace with the actual university ID
     *
     *      UniversityInfoResponseDTO universityInfoResponse = universityService.getUniversityInfoResponseById(universityId);
     *
     *      // Access and process data from the response object.
     *      InfoUserResponseDTO userInfo = universityInfoResponse.getUserInfo();
     *      InfoUniversityResponseDTO universityDetails = universityInfoResponse.getUniversityInfo();
     *
     *      // ... further operations using userInfo and universityDetails
     *  } catch (ResourceNotFoundException e) {
     *      // Handle the scenario where either user or university information is not found
     *      //  - Log the error (this is already done within the findById methods)
     *      //  - Return an error response, if applicable
     *      //  - Throw a custom exception, if necessary
     *  }
     * }
     * </pre>
     *
     * @param id The ID of the university.
     * @return A {@link UniversityInfoResponseDTO} containing combined
     *         user and university-specific data.
     *
     * @see UniversityInfoResponseDTO
     * @see InfoUserResponseDTO
     * @see InfoUniversityResponseDTO
     */
    public UniversityInfoResponseDTO getUniversityInfoResponseById(Integer id) throws ResourceNotFoundException;
    /**
     * Retrieves a {@link UniversityInfo} entity by its unique identifier (ID).
     *
     * <p>This method attempts to find and retrieve University information
     * from the data store using the provided `id`.
     *
     * <p>Example Usage:
     * <pre>
     * {@code
     * try {
     *     Integer universityId = 123; // Replace with the actual university ID
     *     UniversityInfo universityInfo = universityInfoService.findById(universityId);
     *     // ... Use the retrieved universityInfo object
     * } catch (ResourceNotFoundException e) {
     *     // Handle the scenario where the university information is not found.
     *     //  - Log the error (which is already done within the method)
     *     //  - Throw a custom exception or return an error response
     * }
     * }
     * </pre>
     *
     * @param id The unique identifier of the university.
     * @return  The {@link UniversityInfo} object if found.
     * @throws ResourceNotFoundException If no university information is found
     *                                    matching the given ID.
     *
     * @see UniversityInfo
     */
    public UniversityInfo findById(Integer id) throws ResourceNotFoundException;

    //TODO: javadoc
    public ResponseData updateUniversityStatus(Integer id, AccountStatus status) throws ResourceNotFoundException, StoreDataFailedException;
}
