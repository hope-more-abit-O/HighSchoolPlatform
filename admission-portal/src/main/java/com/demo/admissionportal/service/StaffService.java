package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.dto.request.ActiveStaffRequest;
import com.demo.admissionportal.dto.request.DeleteStaffRequest;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.RegisterStaffResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.StaffResponseDTO;
import com.demo.admissionportal.dto.response.staff.FindAllStaffResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <h1>Staff Service Interface</h1>
 * <p>
 * This interface defines the operations related to managing staff within the Admission Portal.
 * It provides methods for registering, updating, retrieving, and deleting staff, as well as activating
 * and listing staff members. Implementations of this interface are responsible for the business logic
 * associated with staff management and interactions with the underlying data layer.
 * </p>
 * <p>
 * The following operations are supported:
 * <ul>
 *     <li>Registering a new staff member</li>
 *     <li>Updating an existing staff member</li>
 *     <li>Retrieving details of a specific staff member by their ID</li>
 *     <li>Listing all staff members with optional filters</li>
 *     <li>Deleting a staff member</li>
 *     <li>Activating a staff member</li>
 * </ul>
 * </p>
 *
 * @since 1.0
 */
public interface StaffService {
    /**
     * <h2>Register Staff</h2>
     * <p>
     * Registers a new staff member based on the provided request data. The request must contain valid staff
     * information, including email, phone, and address. The response includes the result of the registration
     * operation and details of the created staff member.
     * </p>
     *
     * @param request The {@link RegisterStaffRequestDTO} containing the details of the new staff member.
     * @return A {@link ResponseData} object containing the result of the registration operation.
     * @since 1.0
     */
    ResponseData registerStaff(RegisterStaffRequestDTO request);

    /**
     * <h2>Find All Staff</h2>
     * <p>
     * Retrieves a paginated list of all staff members, optionally filtered by username, first name, middle name,
     * last name, email, phone, and status. The response includes summary details of each staff member.
     * </p>
     *
     * @param username   Optional filter for the username.
     * @param firstName  Optional filter for the first name.
     * @param middleName Optional filter for the middle name.
     * @param lastName   Optional filter for the last name.
     * @param email      Optional filter for the email.
     * @param phone      Optional filter for the phone.
     * @param status     Optional filter for the account status.
     * @param pageable   Pagination details.
     * @return A {@link ResponseData} object containing a paginated list of staff members.
     * @since 1.0
     */
    ResponseData<Page<FindAllStaffResponse>> findAll(String username, String firstName, String middleName, String lastName, String email, String phone, AccountStatus status, Pageable pageable);

    /**
     * <h2>Get Staff By ID</h2>
     * <p>
     * Retrieves the details of a specific staff member identified by the provided ID. The response includes
     * all the details of the staff member. If the staff member is not found, an appropriate response will be returned.
     * </p>
     *
     * @param id The ID of the staff member to be retrieved.
     * @return A {@link ResponseData} object containing the details of the staff member.
     * @since 1.0
     */
    ResponseData<?> getStaffById(int id);

    /**
     * <h2>Update Staff</h2>
     * <p>
     * Updates the details of an existing staff member identified by the provided ID. The request contains the updated
     * staff details. The authenticated user's token is used to verify permissions.
     * </p>
     *
     * @param request The {@link UpdateStaffRequestDTO} containing the updated details of the staff member.
     * @param id      The ID of the staff member to be updated.
     * @param token   The authentication token of the user performing the update.
     * @return A {@link ResponseData} object containing the result of the update operation along with the updated {@link StaffResponseDTO}.
     * @since 1.0
     */
    ResponseData<StaffResponseDTO> updateStaff(UpdateStaffRequestDTO request, Integer id, String token);

    /**
     * <h2>Delete Staff By ID</h2>
     * <p>
     * Deletes a specific staff member identified by the provided ID. The staff member will be marked as inactive
     * in the system. The authenticated user's token is used to verify permissions.
     * </p>
     *
     * @param id      The ID of the staff member to be deleted.
     * @param request The {@link DeleteStaffRequest} containing additional information for the deletion operation.
     * @return A {@link ResponseData} object containing the result of the deletion operation.
     * @since 1.0
     */
    ResponseData<?> deleteStaffById(int id, DeleteStaffRequest request);

    /**
     * <h2>Activate Staff By ID</h2>
     * <p>
     * Activates a specific staff member identified by the provided ID, making them active in the system.
     * The authenticated user's token is used to verify permissions. If the staff member is already active,
     * an appropriate response will be returned.
     * </p>
     *
     * @param id      The ID of the staff member to be activated.
     * @param request The {@link ActiveStaffRequest} containing additional information for the activation operation.
     * @return A {@link ResponseData} object containing the result of the activation operation.
     * @since 1.0
     */
    ResponseData<?> activateStaffById(int id, ActiveStaffRequest request);
}
