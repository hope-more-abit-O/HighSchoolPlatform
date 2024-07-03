package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.request.CreateUniversityRequestRequest;
import com.demo.admissionportal.dto.response.PostCreateUniversityRequestResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.CreateUniversityRequest;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.CreateUniversityRequestRepository;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.CreateUniversityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateUniversityServiceImpl implements CreateUniversityService {
    private final CreateUniversityRequestRepository createUniversityRequestRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UniversityInfoRepository universityInfoRepository;

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
    public ResponseData<PostCreateUniversityRequestResponse> createCreateUniversityRequest(CreateUniversityRequestRequest request){
        //Get staff's principal
        User staffAccount = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
                .createBy(staffAccount.getId())
                .createTime(new Date())
                .build();
        try{
            CreateUniversityRequest result = createUniversityRequestRepository.save(createUniversityRequest);
            PostCreateUniversityRequestResponse postCreateUniversityRequestResponseDTO = modelMapper.map(result, PostCreateUniversityRequestResponse.class);
            return ResponseData.ok("Tạo yêu cầu tạo trường thành công!", postCreateUniversityRequestResponseDTO);
        } catch (Exception e){
            return ResponseData.error(e.getMessage());
        }
    }


    @Transactional
    public ResponseData adminAction(Integer id, CreateUniversityRequestStatus status){
        Integer adminId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Optional<CreateUniversityRequest> optionalCreateUniversityRequest = createUniversityRequestRepository.findById(id);
        if (optionalCreateUniversityRequest.isEmpty()) {
            //TODO: UPDATE
            return null;
        }
        CreateUniversityRequest createUniversityRequest = optionalCreateUniversityRequest.get();

        if (status.equals(CreateUniversityRequestStatus.ACCEPTED)){
            User uni = userRepository.save(
                    new User(createUniversityRequest.getUniversityUsername(),
                            createUniversityRequest.getUniversityEmail(),
                            passwordEncoder.encode(UUID.randomUUID().toString()),
                            Role.UNIVERSITY,
                            adminId
                            )
            );

            UniversityInfo universityInfo = new UniversityInfo(uni.getId(), createUniversityRequest);
            universityInfoRepository.save(universityInfo);
        }

        createUniversityRequest.setStatus(status);
        createUniversityRequest.setUpdateBy(adminId);
        createUniversityRequest.setUpdateTime(new Date());
        createUniversityRequest.setConfirmBy(adminId);

        createUniversityRequestRepository.save(createUniversityRequest);

        return ResponseData.ok("Cập nhập yêu cầu tạo trường thành công.");
    }
}
