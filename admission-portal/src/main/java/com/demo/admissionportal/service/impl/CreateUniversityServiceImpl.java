package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.*;
import com.demo.admissionportal.controller.CreateUniversityController;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.create_university_request.CreateUniversityRequestDTO;
import com.demo.admissionportal.dto.request.create_univeristy_request.CreateUniversityRequestRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.CreateUniversityRequest;
import com.demo.admissionportal.entity.UniversityCampus;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.exception.exceptions.QueryException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.repository.CreateUniversityRequestRepository;
import com.demo.admissionportal.repository.UniversityCampusRepository;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.CreateUniversityService;
import com.demo.admissionportal.service.UniversityService;
import com.demo.admissionportal.service.ValidationService;
import com.demo.admissionportal.util.impl.EmailUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final UserServiceImpl userServiceImpl;
    private final CreateUniversityController createUniversityController;
    private final UniversityService universityService;
    private final UniversityCampusRepository universityCampusRepository;

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
    public ResponseData createCreateUniversityRequest(CreateUniversityRequestRequest request)
            throws DataExistedException, StoreDataFailedException {
        //Get staff's principal
        Integer staffId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        log.info("Get Staff ID: {}", staffId);

        validationService.validateCreateUniversityRequest(request.getUniversityUsername(), request.getUniversityEmail(), request.getUniversityCode());

        //Create model
        CreateUniversityRequest createUniversityRequest = CreateUniversityRequest.builder()
                .universityName(request.getUniversityName())
                .universityCode(request.getUniversityCode())
                .universityEmail(request.getUniversityEmail())
                .universityUsername(request.getUniversityUsername())
                .universityType(request.getUniversityType())
                .note(request.getNote())
                .documents(request.getDocuments())
                .status(CreateUniversityRequestStatus.PENDING)
                .createBy(staffId)
                .createTime(new Date())
                .build();
        try {
            CreateUniversityRequest result = createUniversityRequestRepository.save(createUniversityRequest);
            return ResponseData.ok("Tạo yêu cầu tạo trường thành công.", mapping(result));
        } catch (Exception e) {
            throw new StoreDataFailedException("Tạo yêu cầu tạo trường thất bại.");
        }
    }

    @Transactional
    public ResponseData createUniversity(CreateUniversityRequestRequest request)
            throws DataExistedException, StoreDataFailedException {
        //Get staff's principal
        Integer staffId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        log.info("Get Staff ID: {}", staffId);

        validationService.validateCreateUniversityRequest(request.getUniversityUsername(), request.getUniversityEmail(), request.getUniversityCode());

        try {
            universityService.createUniversity(request);
            return ResponseData.ok("Tạo yêu cầu tạo trường thành công.");
        } catch (StoreDataFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new StoreDataFailedException("Tạo yêu cầu tạo trường thất bại.", Map.of("error", e.getCause().getMessage()));
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
     * @param id     The unique identifier (ID) of the university creation request.
     * @param status The new status of the request (`CreateUniversityRequestStatus`).
     * @return A {@link ResponseData} object indicating success message ("Cập nhật yêu cầu tạo trường thành công.").
     * @throws ResourceNotFoundException If the request with the provided ID is not found.
     * @throws StoreDataFailedException  If data persistence fails during the operation.
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

        log.info("Request status {}", createUniversityRequest.getStatus());
        if (!createUniversityRequest.getStatus().equals(CreateUniversityRequestStatus.PENDING)) {
            log.error("Request status {} not available to update!", createUniversityRequest.getStatus());
            return new ResponseData(ResponseCode.C201.getCode()
                    , "Đơn tạo trường đã kết thúc!"
                    , Map.of("createUniversityRequestStauts", createUniversityRequest.getStatus().name()));
        }


        log.info("Saving to database.");
        User uni = null;
        String password = "";
        try {
            if (status.equals(CreateUniversityRequestStatus.ACCEPTED)) {
                validationService.validateCreateUniversity(createUniversityRequest.getUniversityUsername(), createUniversityRequest.getUniversityEmail(), createUniversityRequest.getUniversityCode());

                password = RandomStringUtils.randomAlphanumeric(9);
                log.info("Checking username: {}, email: {} available.", createUniversityRequest.getUniversityUsername(), createUniversityRequest.getUniversityEmail());
                log.info("Check username, email available succeed.");

                log.info("Creating and storing University Account");
                uni = userRepository.save(
                        new User(createUniversityRequest.getUniversityUsername(),
                                createUniversityRequest.getUniversityEmail(),
                                passwordEncoder.encode(password),
                                Role.UNIVERSITY,
                                createUniversityRequest.getCreateBy()
                        )
                );
                log.info("Creating and storing University Account succeed");

                log.info("Creating and storing University Information");
                UniversityInfo universityInfo = new UniversityInfo(uni.getId(), createUniversityRequest);
                universityInfoRepository.save(universityInfo);
                log.info("Creating and storing University Information succeed");
                log.info("Password: {}", password);

                log.info("Start process create university campus");
                UniversityCampus campus = getUniversityCampus(universityInfo);
                universityCampusRepository.save(campus);
                log.info("End process create university campus");
            }

            log.info("Updating and storing Create university request");
            createUniversityRequest.setStatus(status);
            createUniversityRequest.setUpdateBy(adminId);
            createUniversityRequest.setUpdateTime(new Date());
            createUniversityRequest.setConfirmBy(adminId);
            createUniversityRequest.setAdminNote(note);

            createUniversityRequestRepository.save(createUniversityRequest);
            log.info("Updating and storing Create university request succeed");

            if (uni != null) {
                emailUtil.sendAccountPasswordRegister(uni, password);
                return ResponseData.ok("Tạo tài khoản trường học thành công.", userServiceImpl.mappingFullResponse(uni));
            }
            return ResponseData.ok("Từ chối yêu cầu tạo tài khoản trường học thành công.");
        } catch (DataExistedException e) {
            throw new DataExistedException(e.getMessage(), e.getErrors());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreDataFailedException("Tạo tài khoản trường học thất bại.");
        }

    }

    private static UniversityCampus getUniversityCampus(UniversityInfo universityInfo) {
        UniversityCampus campus = new UniversityCampus();
        campus.setUniversityId(universityInfo.getId());
        campus.setCampusName("N/A");
        campus.setEmail("N/A");
        campus.setSpecificAddress("N/A");
        campus.setProvinceId(1);
        campus.setDistrictId(1);
        campus.setWardId(1);
        campus.setCreateTime(new Date());
        campus.setCreateBy(universityInfo.getId());
        campus.setPicture("https://firebasestorage.googleapis.com/v0/b/highschoolvn-dev.appspot.com/o/campus-images%2Fcampus-image-default.jpg?alt=media&token=ff47748d-be85-4871-8abd-c214a2ee6f1e");
        campus.setType(CampusType.HEADQUARTERS);
        campus.setStatus(UniversityCampusStatus.ACTIVE);
        return campus;
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
     * @param id The unique identifier (ID) of the request.
     * @return The {@link CreateUniversityRequest} if found.
     * @throws ResourceNotFoundException If no matching university
     *                                   creation request is found.
     * @see CreateUniversityRequest
     */
    public CreateUniversityRequest findById(Integer id) throws ResourceNotFoundException {
        return createUniversityRequestRepository.findById(id).orElseThrow(() -> {
            log.error("Create university request with id: {} not found.", id);
            return new ResourceNotFoundException("Không tìm thấy yêu cầu tạo tài khoản trường học với mã: " + id);
        });
    }

    public CreateUniversityRequestDTO getById(Integer id) throws ResourceNotFoundException {
        CreateUniversityRequest createUniversityRequest = findById(id);

        return mapping(createUniversityRequest);
    }

    public ResponseData<Page<CreateUniversityRequestDTO>> getByStaff(Pageable pageable) {
        Integer staffId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Page<CreateUniversityRequest> createUniversityRequests = createUniversityRequestRepository.findByCreateBy(staffId, pageable);

        List<ActionerDTO> actionerDTOs = this.getActioners(createUniversityRequests.getContent());

        Page<CreateUniversityRequestDTO> mappedRequests = createUniversityRequests.map(request -> this.mapping(request, actionerDTOs));

        return ResponseData.ok("Lấy thông tin yêu cầu tạo trường thành công.", mappedRequests);
    }

    public CreateUniversityRequestDTO mapping(CreateUniversityRequest createUniversityRequest) {
        CreateUniversityRequestDTO result = modelMapper.map(createUniversityRequest, CreateUniversityRequestDTO.class);
        if (createUniversityRequest.getUpdateBy() == null)
            result.setUpdateBy(null);
        if (createUniversityRequest.getConfirmBy() == null)
            result.setConfirmBy(null);

        List<Integer> actionerIds = new ArrayList<>();
        actionerIds.add(createUniversityRequest.getCreateBy());
        if (createUniversityRequest.getUpdateBy() != null)
            actionerIds.add(createUniversityRequest.getUpdateBy());
        if (createUniversityRequest.getConfirmBy() != null)
            actionerIds.add(createUniversityRequest.getConfirmBy());

        List<ActionerDTO> actioners = userServiceImpl.getActioners(actionerIds);

        for (ActionerDTO actioner : actioners) {
            if (actioner.getId().equals(createUniversityRequest.getCreateBy())) {
                result.setCreateBy(actioner);
            }
            if (createUniversityRequest.getUpdateBy() != null && actioner.getId().equals(createUniversityRequest.getUpdateBy())) {
                result.setUpdateBy(actioner);
            }
            if (createUniversityRequest.getConfirmBy() != null && actioner.getId().equals(createUniversityRequest.getConfirmBy())) {
                result.setConfirmBy(actioner);
            }
        }

        result.setDocuments(Arrays.stream(createUniversityRequest.getDocuments().split(",")).toList());

        return result;
    }

    public CreateUniversityRequestDTO mapping(CreateUniversityRequest createUniversityRequest, List<ActionerDTO> actioners) {
        CreateUniversityRequestDTO result = modelMapper.map(createUniversityRequest, CreateUniversityRequestDTO.class);
        if (createUniversityRequest.getUpdateBy() == null)
            result.setUpdateBy(null);
        if (createUniversityRequest.getConfirmBy() == null)
            result.setConfirmBy(null);

        for (ActionerDTO actioner : actioners) {
            if (actioner.getId().equals(createUniversityRequest.getCreateBy())) {
                result.setCreateBy(actioner);
            }
            if (createUniversityRequest.getUpdateBy() != null && actioner.getId().equals(createUniversityRequest.getUpdateBy())) {
                result.setUpdateBy(actioner);
            }
            if (createUniversityRequest.getConfirmBy() != null && actioner.getId().equals(createUniversityRequest.getConfirmBy())) {
                result.setConfirmBy(actioner);
            }
        }

        result.setDocuments(Arrays.stream(createUniversityRequest.getDocuments().split(",")).toList());

        return result;
    }

    public ResponseData<Page<CreateUniversityRequestDTO>> getBy(Pageable pageable,
                                                                Integer id,
                                                                String universityName,
                                                                String universityCode,
                                                                String universityEmail,
                                                                String universityUsername,
                                                                List<CreateUniversityRequestStatus> status,
                                                                Integer createBy,
                                                                String createByName,
                                                                Integer confirmBy) {

        try {
            List<String> statusStrings = (status == null || status.isEmpty())
                    ? null
                    : status.stream().map(CreateUniversityRequestStatus::name).toList();

            Page<CreateUniversityRequest> createUniversityRequests;

            if (statusStrings != null) {
                createUniversityRequests = createUniversityRequestRepository.findAllBy(pageable,
                        id,
                        universityName,
                        universityCode,
                        universityEmail,
                        universityUsername,
                        statusStrings,
                        createBy,
                        createByName,
                        confirmBy);
            } else {
                createUniversityRequests = createUniversityRequestRepository.findAllBy(pageable,
                        id,
                        universityName,
                        universityCode,
                        universityEmail,
                        universityUsername,
                        createBy,
                        createByName,
                        confirmBy);
            }

            if (createUniversityRequests.getContent().isEmpty())
                return ResponseData.ok("Lấy thông tin yêu cầu tạo trường thành công.");

            List<ActionerDTO> actionerDTOs = this.getActioners(createUniversityRequests.getContent());

            return ResponseData.ok("Lấy thông tin yêu cầu tạo trường thành công.", createUniversityRequests.map((element) -> this.mapping(element, actionerDTOs)));
        } catch (Exception e) {
            throw new QueryException(e.getMessage());
        }
    }

    public List<ActionerDTO> getActioners(List<CreateUniversityRequest> createUniversityRequests) {
        Set<Integer> actionerIds = createUniversityRequests.stream()
                .flatMap((request) -> Stream.of(request.getCreateBy(), request.getUpdateBy(), request.getConfirmBy()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<ActionerDTO> actionerDTOs = userServiceImpl.getActioners(actionerIds.stream().toList());
        return actionerDTOs;
    }
}
