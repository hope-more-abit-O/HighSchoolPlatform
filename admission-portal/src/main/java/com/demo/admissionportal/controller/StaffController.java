package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.entity.create_university_request.CreateUniversityRequestDTO;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.request.*;
import com.demo.admissionportal.dto.request.create_univeristy_request.CreateUniversityRequestRequest;
import com.demo.admissionportal.dto.request.university.UpdateUniversityStatusRequest;
import com.demo.admissionportal.dto.response.*;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO;
import com.demo.admissionportal.dto.response.sub_entity.SubjectResponseDTO;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.exception.exceptions.NotAllowedException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * The type Staff controller.
 */
@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "BearerAuth")
public class StaffController {
    private final StaffService staffService;
    private final UserService userService;
    private final SubjectGroupService subjectGroupService;
    private final SubjectService subjectService;
    private final CreateUniversityService createUniversityService;
    private final UniversityService universityService;
    private final ConsultantService consultantService;

    /**
     * Handles the submission of a university creation request.
     *
     * <p>This endpoint receives a request to create a new university
     * and delegates the processing to the `CreateUniversityService`.
     *
     * @param request The creation request data provided in the request body (JSON).
     * @return A ResponseEntity containing the operation's result (success or failure details) and an appropriate HTTP status code.
     * @throws DataExistedException If the request conflicts with existing data (e.g., duplicate names).
     * @see CreateUniversityRequestRequest
     * @see ResponseData
     */
    @PostMapping("/create-university")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<?> sendCreateUniversityRequest(@RequestBody @Valid CreateUniversityRequestRequest request) {
        ResponseData<PostCreateUniversityRequestResponse> response = createUniversityService.createCreateUniversityRequest(request);
        if (response.getStatus() != ResponseCode.C200.getCode())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.getMessage());
        return ResponseEntity.ok(response);
    }

    /**
     * Gets create university requests.
     *
     * @param pageable           the pageable
     * @param id                 the id
     * @param universityName     the university name
     * @param universityCode     the university code
     * @param universityEmail    the university email
     * @param universityUsername the university username
     * @param status             the status
     * @param confirmBy          the confirm by
     * @return the create university requests
     */
    @GetMapping("/create-university-request")
    public ResponseEntity<ResponseData<Page<CreateUniversityRequestDTO>>> getCreateUniversityRequests(Pageable pageable,
                                                                                                      @RequestParam(required = false) Integer id,
                                                                                                      @RequestParam(required = false) String universityName,
                                                                                                      @RequestParam(required = false) String universityCode,
                                                                                                      @RequestParam(required = false) String universityEmail,
                                                                                                      @RequestParam(required = false) String universityUsername,
                                                                                                      @RequestParam(required = false) CreateUniversityRequestStatus status,
                                                                                                      @RequestParam(required = false) Integer confirmBy) {
        Integer staffId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(createUniversityService.getBy(pageable, id, universityName, universityCode, universityEmail, universityUsername, status, staffId, null, confirmBy));
    }

    /**
     * Gets university management.
     *
     * @param pageable the pageable
     * @param id       the id
     * @param code     the code
     * @param username the username
     * @param name     the name
     * @param phone    the phone
     * @param email    the email
     * @param status   the status
     * @return the university management
     */
    @GetMapping("/university/management")
    public ResponseEntity<ResponseData<Page<UniversityFullResponseDTO>>> getUniversityManagement(
            Pageable pageable,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) AccountStatus status) {
        return ResponseEntity.ok(universityService.getUniversityFullResponseByStaffId(pageable, id, code, username, name, phone, email, status));
    }

    /**
     * Retrieves a staff member's details by their ID.
     *
     * <p>Fetches detailed information about a staff member using their unique identifier and returns
     * the data as a response.
     *
     * @param id The unique identifier of the staff member to retrieve.
     * @return A ResponseEntity containing staff details with a suitable HTTP status (e.g., 200 OK for success, 404 Not Found if the staff member is not found).
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<?> getStaffById(@PathVariable int id) {
        ResponseData<?> result = staffService.getStaffById(id);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff Get by ID successfully: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found by ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to Get staff by ID: {}", id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Updates the details of a staff member.
     *
     * <p>Receives updated staff details and processes the update request.
     *
     * @param request The request containing the updated staff details.
     * @param id      The unique identifier of the staff member to update.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<StaffResponseDTO>> updateStaff(@RequestBody @Valid UpdateStaffRequestDTO request, @PathVariable Integer id) {
        log.info("Received request to update staff: {}", request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        ResponseData<StaffResponseDTO> result = staffService.updateStaff(request, id, username);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff updated successfully with ID: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        log.error("Failed to update staff: {}", request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Retrieves a list of users based on search criteria.
     *
     * <p>Fetches a paginated list of users based on optional username and email parameters.
     *
     * @param username        The username to search for (optional).
     * @param firstName       the first name
     * @param middleName      the middle name
     * @param lastName        the last name
     * @param phone           the phone
     * @param email           the gender
     * @param specificAddress the specific address
     * @param educationLevel  the education level
     * @param status          the status
     * @param pageable        The pagination information.
     * @return A ResponseEntity containing the paginated list of users and a suitable HTTP status.
     */
    @GetMapping("/list/users")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<UserResponseDTO>>> getUser(@RequestParam(required = false) String username,
                                                                       @RequestParam(required = false) String firstName,
                                                                       @RequestParam(required = false) String middleName,
                                                                       @RequestParam(required = false) String lastName,
                                                                       @RequestParam(required = false) String phone,
                                                                       @RequestParam(required = false) String email,
                                                                       @RequestParam(required = false) String specificAddress,
                                                                       @RequestParam(required = false) String educationLevel,
                                                                       @RequestParam(required = false) String status,
                                                                       Pageable pageable) {
        ResponseData<Page<UserResponseDTO>> user = userService.getUser(username, firstName, middleName, lastName, phone, email, specificAddress, educationLevel, status, pageable);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }

    /**
     * Changes the status of a user.
     *
     * <p>Receives a request to change the status of a user and processes it.
     *
     * @param id         The unique identifier of the user to change the status of.
     * @param requestDTO The request containing the new status details.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     */
    @PostMapping("/user/change-status/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<ChangeStatusUserResponseDTO>> changeStatus(@PathVariable("id") Integer id, @RequestBody ChangeStatusUserRequestDTO requestDTO) {
        if (id == null || id < 0 || requestDTO == null) {
            return ResponseEntity.badRequest().body(new ResponseData<>(ResponseCode.C205.getCode(), "Invalid request"));
        }
        ResponseData<ChangeStatusUserResponseDTO> user = userService.changeStatus(id, requestDTO);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }

    /**
     * Retrieves a list of all subjects based on search criteria.
     *
     * <p>Fetches a paginated list of subjects based on optional name and status parameters.
     *
     * @param name     The name of the subject to search for (optional).
     * @param status   The status of the subject to search for (optional).
     * @param pageable The pagination information.
     * @return A ResponseEntity containing the paginated list of subjects and a suitable HTTP status.
     */
    @GetMapping("/list-all-subjects")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<SubjectResponseDTO>>> findAllSubjects(@RequestParam(required = false) String name, @RequestParam(required = false) SubjectStatus status, @PageableDefault(size = 10) Pageable pageable) {
        ResponseData<Page<SubjectResponseDTO>> result = subjectService.findAll(name, status, pageable);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Retrieves a subject's details by its ID.
     *
     * <p>Fetches detailed information about a subject using its unique identifier and returns
     * the data as a response.
     *
     * @param id The unique identifier of the subject to retrieve.
     * @return A ResponseEntity containing subject details with a suitable HTTP status (e.g., 200 OK for success, 404 Not Found if the subject is not found).
     */
    @GetMapping("/get-subject/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<SubjectResponseDTO>> getSubjectById(@PathVariable Integer id) {
        ResponseData<SubjectResponseDTO> response = subjectService.getSubjectById(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves university details by its ID.
     *
     * <p>Fetches detailed information about a university using its unique identifier and returns
     * the data as a response.
     *
     * @param id The unique identifier of the university to retrieve.
     * @return A ResponseEntity containing university details with a suitable HTTP status.
     */
    @GetMapping("/university/{id}")
    public ResponseEntity<UniversityFullResponseDTO> getUniversityInfoById(@PathVariable Integer id) {
        return ResponseEntity.ok(universityService.getUniversityFullResponseById(id));
    }

    /**
     * Creates a new subject.
     *
     * <p>Receives a request to create a new subject and processes it.
     *
     * @param requestSubjectDTO The request containing the subject details.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     */
    @PostMapping("/create-subject")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Subject>> createSubject(@RequestBody @Valid RequestSubjectDTO requestSubjectDTO) {
        ResponseData<Subject> createdSubject = subjectService.createSubject(requestSubjectDTO);
        if (createdSubject != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSubject);
        } else {
            return ResponseEntity.status(createdSubject.getStatus()).body(createdSubject);
        }
    }

    /**
     * Activates a subject by its ID.
     *
     * <p>Receives a request to activate a subject and processes it.
     *
     * @param id The unique identifier of the subject to activate.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     */
    @PutMapping("/activate-subject/{id}")
    public ResponseEntity<?> activateSubject(@PathVariable @Valid Integer id) {
        ResponseData<?> response = subjectService.activateSubject(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Deletes a subject by its ID.
     *
     * <p>Receives a request to delete a subject and processes it.
     *
     * @param id The unique identifier of the subject to delete.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     */
    @DeleteMapping("/delete-subject/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable @Valid Integer id) {
        ResponseData<?> response = subjectService.deleteSubject(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Creates a new subject group.
     *
     * <p>Receives a request to create a new subject group and processes it.
     *
     * @param request The request containing the subject group details.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     */
    @PostMapping("/create-subject-group")
    public ResponseEntity<ResponseData<?>> createSubjectGroup(@RequestBody @Valid CreateSubjectGroupRequestDTO request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(new ResponseData<>(ResponseCode.C205.getCode(), "Request cannot be null or empty"));
        }
        ResponseData<?> response = subjectGroupService.createSubjectGroup(request);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Updates the details of a subject group.
     *
     * <p>Receives updated subject group details and processes the update request.
     *
     * @param id      The unique identifier of the subject group to update.
     * @param request The request containing the updated subject group details.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     */
    @PutMapping("/update-subject-group/{id}")
    public ResponseEntity<ResponseData<?>> updateSubjectGroup(@PathVariable Integer id, @RequestBody UpdateSubjectGroupRequestDTO request) {
        ResponseData<?> response = subjectGroupService.updateSubjectGroup(id, request);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves a subject group's details by its ID.
     *
     * <p>Fetches detailed information about a subject group using its unique identifier and returns
     * the data as a response.
     *
     * @param id The unique identifier of the subject group to retrieve.
     * @return A ResponseEntity containing subject group details with a suitable HTTP status.
     */
    @GetMapping("/get-subject-group/{id}")
    public ResponseEntity<ResponseData<?>> getSubjectGroupById(@PathVariable Integer id) {
        ResponseData<?> response = subjectGroupService.getSubjectGroupById(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves a list of all subject groups based on search criteria.
     *
     * <p>Fetches a paginated list of subject groups based on optional group name, subject name, and status parameters.
     *
     * @param groupName   The name of the group to search for (optional).
     * @param subjectName The name of the subject to search for (optional).
     * @param status      The status of the subject group to search for (optional).
     * @param pageable    The pagination information.
     * @return A ResponseEntity containing the paginated list of subject groups and a suitable HTTP status.
     */
    @GetMapping("/list-all-subject-groups")
    public ResponseEntity<ResponseData<Page<SubjectGroupResponseDTO>>> findAllSubjectGroups(@RequestParam(required = false) String groupName, @RequestParam(required = false) String subjectName, @RequestParam(required = false) SubjectStatus status, Pageable pageable) {
        ResponseData<Page<SubjectGroupResponseDTO>> result = subjectGroupService.findAll(groupName, subjectName, status, pageable);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Deletes a subject group by its ID.
     *
     * <p>Receives a request to delete a subject group and processes it.
     *
     * @param id The unique identifier of the subject group to delete.
     * @param id The unique identifier of the subject group to activate.
     * @param id The unique identifier of the subject group to delete.
     * @param id The unique identifier of the subject group to activate.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     * @throws NotAllowedException       the not allowed exception
     * @throws ResourceNotFoundException the resource not found exception                                   <p>                                   Activates a subject group by its ID.                                   <p>Receives a request to activate a subject group and processes it.
     *                                   <p>
     *                                   Activates a subject group by its ID.
     *
     *                                   <p>Receives a request to activate a subject group and processes it.
     * @throws NotAllowedException       the not allowed exception
     * @throws ResourceNotFoundException the resource not found exception
     *                                   <p>
     *                                   Deletes a subject group by its ID.
     *
     *                                   <p>Receives a request to delete a subject group and processes it.
     * @throws NotAllowedException       the not allowed exception
     * @throws ResourceNotFoundException the resource not found exception
     *                                   <p>
     *                                   Activates a subject group by its ID.
     *
     *                                   <p>Receives a request to activate a subject group and processes it.
     */
    /**
     * Activates a subject group by its ID.
     *
     * <p>Receives a request to activate a subject group and processes it.
     *
     * @param id The unique identifier of the subject group to activate.
     * @return A ResponseEntity containing the operation's result and a suitable HTTP status.
     * @throws NotAllowedException       the not allowed exception
     * @throws ResourceNotFoundException the resource not found exception                                   <p>                                   Deletes a subject group by its ID.                                   <p>Receives a request to delete a subject group and processes it.
     * @throws NotAllowedException       the not allowed exception
     * @throws ResourceNotFoundException the resource not found exception                                   <p>                                   Activates a subject group by its ID.                                   <p>Receives a request to activate a subject group and processes it.
     */
    @PutMapping("/activate-subject-group/{id}")
    public ResponseEntity<ResponseData<?>> activateSubjectGroup(@PathVariable Integer id) {
        ResponseData<?> result = subjectGroupService.activateSubjectGroup(id);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Delete subject group response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/delete-subject-group/{id}")
    public ResponseEntity<?> deleteSubjectGroup(@PathVariable @Valid Integer id) {
        ResponseData<?> response = subjectGroupService.deleteSubjectGroup(id);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     * @throws NotAllowedException       the not allowed exception
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/consultant/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) throws NotAllowedException, ResourceNotFoundException {
        return ResponseEntity.ok(consultantService.getFullConsultantById(id));
    }

    @PatchMapping("/university/change-status/{id}")
    public ResponseEntity<ResponseData> activeUniversityById(@PathVariable Integer id, @RequestBody UpdateUniversityStatusRequest request) throws ResourceNotFoundException, StoreDataFailedException {
        return ResponseEntity.ok(universityService.updateUniversityStatus(id, request.note()));
    }
}
