package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.entity.consultant.FullConsultantResponseDTO;
import com.demo.admissionportal.dto.request.consultant.CreateConsultantRequest;
import com.demo.admissionportal.dto.request.consultant.SelfUpdateConsultantInfoRequest;
import com.demo.admissionportal.dto.request.consultant.UpdateConsultantAddressRequest;
import com.demo.admissionportal.dto.request.consultant.UpdateConsultantInfoByIdRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.consultant.ChangeConsultantStatusRequest;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.NotAllowedException;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import org.apache.coyote.BadRequestException;

/**
 * Defines the contract for managing consultant information, including creation and retrieval.
 */
public interface ConsultantService {

    /**
     * Creates a new consultant account and associated information.
     *
     * <p>This method handles the entire process of creating a new consultant,
     * from validating the provided data to persisting the information in the system.
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * CreateConsultantRequest request = new CreateConsultantRequest();
     * // ... populate request with consultant details ...
     *
     * try {
     *     ResponseData response = consultantInfoService.createConsultant(request);
     *     // ... process the success response
     * } catch (DataExistedException | StoreDataFailedException | ResourceNotFoundException e) {
     *     // ... handle the exceptions appropriately
     * }
     * }
     * </pre>
     *
     * @param request  The {@link CreateConsultantRequest} containing details
     *                 for the new consultant.
     * @return A {@link ResponseData} indicating the result of the
     *         creation operation (success or failure).
     * @throws DataExistedException      If the provided data conflicts
     *                                    with existing records (e.g., duplicate username).
     * @throws StoreDataFailedException If an error occurs while saving consultant data.
     * @throws ResourceNotFoundException If dependent resources (e.g., related entities)
     *                                    cannot be found.
     *
     * @see CreateConsultantRequest
     * @see ResponseData
     */
    ResponseData createConsultant(CreateConsultantRequest request)
            throws DataExistedException, StoreDataFailedException, ResourceNotFoundException;

    /**
     * Retrieves complete details for a consultant by their unique ID.
     *
     * <p>This method fetches a comprehensive representation of
     * the consultant identified by the provided `id`.
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * Integer consultantId = 123;
     *
     * try {
     *     ConsultantFullResponseDTO consultant = consultantInfoService.getById(consultantId);
     *     // ... process the retrieved consultant details
     * } catch (ResourceNotFoundException e) {
     *     // ... handle the case where the consultant is not found
     * }
     * }
     * </pre>
     *
     * @param id The unique identifier of the consultant.
     * @return A {@link FullConsultantResponseDTO} containing
     *         comprehensive consultant details.
     * @throws ResourceNotFoundException If no consultant is found with
     *                                    the specified `id`.
     *
     * @see FullConsultantResponseDTO
     */
    FullConsultantResponseDTO getFullConsultantById(Integer id) throws ResourceNotFoundException;

    ResponseData selfUpdateConsultantInfo(SelfUpdateConsultantInfoRequest request) throws ResourceNotFoundException, StoreDataFailedException;

    ResponseData updateConsultantInfoById(UpdateConsultantInfoByIdRequest request) throws ResourceNotFoundException, StoreDataFailedException;

    ResponseData updateConsultantStatus(Integer id, ChangeConsultantStatusRequest request) throws NotAllowedException,ResourceNotFoundException, BadRequestException, StoreDataFailedException;
    ResponseData selfUpdateConsultantAddress(UpdateConsultantAddressRequest request) throws ResourceNotFoundException, StoreDataFailedException;}