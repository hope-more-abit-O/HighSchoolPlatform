package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.request.create_univeristy_request.CreateUniversityRequestRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.CreateUniversityRequest;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.repository.CreateUniversityRequestRepository;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.CreateUniversityService;
import com.demo.admissionportal.service.ValidationService;
import com.demo.admissionportal.util.impl.EmailUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUniversityServiceImpl implements CreateUniversityService {
    private final CreateUniversityRequestRepository createUniversityRequestRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UniversityInfoRepository universityInfoRepository;
    private final ValidationService validationService;
    private final EmailUtil emailUtil;

    /**
     * Handles the creation of a university creation request.
     *
     * <p> This method receives a {@link CreateUniversityRequestRequest} object containing the necessary
     * information for the request. It retrieves the authenticated staff member's details from the
     * security context and constructs a {@link CreateUniversityRequest} object. The request is then
     * persisted, and a response entity indicating successful processing is returned.
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * CreateUniversityRequestRequestDTO requestDTO = new CreateUniversityRequestRequestDTO();
     * requestDTO.setUniversityName("Example University");
     * // ... set other fields in requestDTO ...
     *
     * ResponseEntity<CreateUniversityRequest> response = createCreateUniversityRequest(requestDTO);
     *
     * if (response.getStatusCode() == HttpStatus.OK) {
     *     CreateUniversityRequest createdRequest = response.getBody();
     *     // ... further processing with the created request ...
     * } else {
     *     // ... handle errors ...
     * }
     * }
     * </pre>
     *
     * @param request The data transfer object containing the university creation request details.
     * @return A {@link ResponseEntity} with an HTTP status of 200 (OK) if the request is successfully processed.
     * The response body contains the persisted {@link CreateUniversityRequest} object.
     * @throws NullPointerException If the provided {@code request} is {@code null}.
     * @see CreateUniversityRequestRequest
     * @see CreateUniversityRequest
     * @see CreateUniversityRequestStatus
     * @since 1.0
     */
    @Transactional
    public ResponseData createCreateUniversityRequest(CreateUniversityRequestRequest request) throws StoreDataFailedException{
        //Get staff's principal
        Integer staffId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        log.info("Get Staff ID: {}", staffId);

        //Create model
        CreateUniversityRequest createUniversityRequest = CreateUniversityRequest.builder()
                .universityName(request.getUniversityName())
                .universityCode(request.getUniversityCode())
                .universityEmail(request.getUniversityEmail())
                .universityUsername(request.getUniversityUsername())
                .universityType(UniversityType.valueOf(request.getUniversityType()))
                .note(request.getNote())
                .documents(request.getDocuments())
                .status(CreateUniversityRequestStatus.PENDING)
                .createBy(staffId)
                .createTime(new Date())
                .build();
        try{
            CreateUniversityRequest result = createUniversityRequestRepository.save(createUniversityRequest);
            return ResponseData.ok("Tạo yêu cầu tạo trường thành công.", createUniversityRequest.getId());
        } catch (Exception e){
            throw new StoreDataFailedException("Tạo yêu cầu tạo trường thất bại.");
        }
    }

    /**
     * Processes an admin action on a university creation request.
     *
     * <p>This method performs various actions on a university creation request identified by its `id`
     * depending on the provided `status`. It retrieves the request, validates its username and email,
     * and potentially creates a university account and university information upon approval. The
     * corresponding request status and admin performing the action are also updated.
     *
     * <p>**Note:** This method is transactional, meaning any data modifications either succeed entirely
     * or fail entirely.
     *
     * @param id The unique identifier (ID) of the university creation request.
     * @param status The new status of the request (`CreateUniversityRequestStatus`).
     * @return A {@link ResponseData} object indicating success message ("Cập nhập yêu cầu tạo trường thành công.").
     * @throws ResourceNotFoundException If the request with the provided ID is not found.
     * @throws StoreDataFailedException If data persistence fails during the operation.
     *
     * @see CreateUniversityRequest
     * @see CreateUniversityRequestStatus
     * @see ResponseData
     * @see ResourceNotFoundException
     * @see StoreDataFailedException
     */
    @Transactional
    @Override
    public ResponseData adminAction(Integer id, CreateUniversityRequestStatus status, String note) throws ResourceNotFoundException, StoreDataFailedException {
        Integer uniId = 0;
        Integer adminId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        log.info("Get Admin ID: {}", adminId);

        log.info("Get CreateUniversityRequest by Id: {}.", id);
        CreateUniversityRequest createUniversityRequest = findById(id);
        log.info("Get CreateUniversityRequest by Id: {} succeed.", id);


        log.info("Checking username: {}, email: {} available.", createUniversityRequest.getUniversityUsername(), createUniversityRequest.getUniversityEmail());
        validationService.validateRegister(createUniversityRequest.getUniversityUsername(), createUniversityRequest.getUniversityEmail());
        log.info("Check username, email available succeed.");

        log.info("Saving to database.");
        User uni = null;
        try{
            if (status.equals(CreateUniversityRequestStatus.ACCEPTED)){
                log.info("Creating and storing University Account");
                uni = userRepository.save(
                        new User(createUniversityRequest.getUniversityUsername(),
                                createUniversityRequest.getUniversityEmail(),
                                passwordEncoder.encode("passgivay"),
                                Role.UNIVERSITY,
                                adminId
                        )
                );
                uniId = uni.getId();
                log.info("Creating and storing University Account succeed");

                log.info("Creating and storing University Information");
                UniversityInfo universityInfo = new UniversityInfo(uni.getId(), createUniversityRequest);
                universityInfoRepository.save(universityInfo);
                log.info("Creating and storing University Information succeed");
            }

            log.info("Updating and storing Create university request");
            createUniversityRequest.setStatus(status);
            createUniversityRequest.setUpdateBy(adminId);
            createUniversityRequest.setUpdateTime(new Date());
            createUniversityRequest.setConfirmBy(adminId);
            createUniversityRequest.setNote(note);

            createUniversityRequestRepository.save(createUniversityRequest);
            if (uni != null)
                emailUtil.sendAccountPasswordRegister(uni, "passlagivay");
            log.info("Updating and storing Create university request succeed");
            return ResponseData.ok("Tạo tài khoản trường học thành công.", uniId);
        } catch (Exception e){
            log.error(e.getMessage());
            throw new StoreDataFailedException("Tạo tài khoản trường học thất bại.");
        }

    }

    /**
     * Finds a university creation request by its ID.
     *
     * <p> This method searches for a university creation request
     * in the system that matches the given `id`. If no such
     * request is found, it throws a `ResourceNotFoundException`.
     *
     * <p> Example Usage:
     * <pre>
     * {@code
     * try {
     *     int requestId = 123;  // The ID of the request you are looking for
     *     CreateUniversityRequest request = createUniversityService.findById(requestId);
     *
     *     // ... process the 'request' object
     *
     * } catch (ResourceNotFoundException e) {
     *     // Handle the case when the request is not found.
     *     // - Log the error
     *     // - Throw a custom exception or return an appropriate error response to the caller.
     *     log.warn("Could not find university request", e); // Log with additional context
     *     // ... more error handling ...
     * }
     * }
     * </pre>
     *
     * @param id  The unique identifier (ID) of the request.
     * @return     The {@link CreateUniversityRequest} if found.
     * @throws ResourceNotFoundException  If no matching university
     *                                   creation request is found.
     *
     * @see CreateUniversityRequest
     */
    public CreateUniversityRequest findById(Integer id) throws ResourceNotFoundException{
        return createUniversityRequestRepository.findById(id).orElseThrow( () -> {
            log.error("Create university request with id: {} not found.", id);
            return new ResourceNotFoundException("Không tìm thấy yêu cầu tạo tài khoản trường học với mã: " + id);
        });
    }
}
