package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.request.UpdateSubjectGroupRequestDTO;
import com.demo.admissionportal.dto.request.CreateSubjectGroupRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * <h1>Subject Group Service Interface</h1>
 * <p>
 * This interface defines the operations related to managing subject groups within the Admission Portal.
 * It provides methods for creating, updating, retrieving, and managing the status of subject groups.
 * Implementations of this interface are responsible for the business logic associated with subject group
 * management and interactions with the underlying data layer.
 * </p>
 * <p>
 * The following operations are supported:
 * <ul>
 *     <li>Creating a new subject group</li>
 *     <li>Updating an existing subject group</li>
 *     <li>Retrieving details of a specific subject group by its ID</li>
 *     <li>Listing all subject groups with optional filters</li>
 *     <li>Deleting a subject group</li>
 *     <li>Activating a subject group</li>
 * </ul>
 * </p>
 *
 * @since 1.0
 */
public interface SubjectGroupService {

    /**
     * <h2>Create Subject Group</h2>
     * <p>
     * Creates a new subject group based on the provided request data. The request must contain valid subject IDs and
     * a unique group name. If the subject group already exists or if any provided subject is inactive, an appropriate
     * response will be returned indicating the error.
     * </p>
     *
     * @param request The {@link CreateSubjectGroupRequestDTO} containing the details of the new subject group.
     * @return A {@link ResponseData} object containing the result of the creation operation.
     * @since 1.0
     */
    ResponseData<?> createSubjectGroup(CreateSubjectGroupRequestDTO request);

    /**
     * <h2>Update Subject Group</h2>
     * <p>
     * Updates the details of an existing subject group identified by the provided ID. The update operation can modify
     * the group's name and its associated subjects. If the subject group or any provided subject is not found, or if the
     * subject group is inactive, an appropriate response will be returned.
     * </p>
     *
     * @param id      The ID of the subject group to be updated.
     * @param request The {@link UpdateSubjectGroupRequestDTO} containing the updated details of the subject group.
     * @return A {@link ResponseData} object containing the result of the update operation.
     * @since 1.0
     */
    ResponseData<?> updateSubjectGroup(Integer id, UpdateSubjectGroupRequestDTO request);

    /**
     * <h2>Get Subject Group By ID</h2>
     * <p>
     * Retrieves the details of a specific subject group identified by the provided ID. The response includes the group's
     * name, status, creation details, and associated subjects. If the subject group is not found, an appropriate response
     * will be returned.
     * </p>
     *
     * @param id The ID of the subject group to be retrieved.
     * @return A {@link ResponseData} object containing the details of the subject group.
     * @since 1.0
     */
    ResponseData<?> getSubjectGroupById(Integer id);

    /**
     * <h2>Find All Subject Groups</h2>
     * <p>
     * Retrieves a paginated list of all subject groups, optionally filtered by group name, subject name, and status.
     * The response includes summary details of each subject group. If no filters are provided, all subject groups will
     * be listed.
     * </p>
     *
     * @param groupName   Optional filter for the group name.
     * @param subjectName Optional filter for the subject name.
     * @param status      Optional filter for the subject group status.
     * @param pageable    Pagination details.
     * @return A {@link ResponseData} object containing a paginated list of subject groups.
     * @since 1.0
     */
    ResponseData<Page<SubjectGroupResponseDTO>> findAll(String groupName, String subjectName, SubjectStatus status, Pageable pageable);

    /**
     * <h2>Delete Subject Group</h2>
     * <p>
     * Marks a specific subject group identified by the provided ID as inactive. The subject group will no longer be
     * considered active in the system. If the subject group is not found or is already inactive, an appropriate response
     * will be returned.
     * </p>
     *
     * @param id The ID of the subject group to be deleted.
     * @return A {@link ResponseData} object containing the result of the deletion operation.
     * @since 1.0
     */
    ResponseData<?> deleteSubjectGroup(Integer id);

    /**
     * <h2>Activate Subject Group</h2>
     * <p>
     * Activates a specific subject group identified by the provided ID, making it active in the system. If the subject
     * group is not found or is already active, an appropriate response will be returned.
     * </p>
     *
     * @param id The ID of the subject group to be activated.
     * @return A {@link ResponseData} object containing the result of the activation operation.
     * @since 1.0
     */
    ResponseData<?> activateSubjectGroup(Integer id);

    List<SubjectGroupResponseDTO2> getAll();
}
