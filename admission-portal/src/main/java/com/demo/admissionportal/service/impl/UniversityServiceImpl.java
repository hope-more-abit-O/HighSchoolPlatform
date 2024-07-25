package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.entity.university.UniversityInfoResponseDTO;
import com.demo.admissionportal.dto.entity.university.FullUniversityResponseDTO;
import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import com.demo.admissionportal.dto.entity.user.FullUserResponseDTO;
import com.demo.admissionportal.dto.request.university.DeleteUniversityRequest;
import com.demo.admissionportal.dto.request.university.UpdateUniversityInfoRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.university.UpdateUniversityInfoResponse;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.NotAllowedException;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.ConsultantService;
import com.demo.admissionportal.service.UniversityInfoService;
import com.demo.admissionportal.service.UniversityService;
import com.demo.admissionportal.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides methods for managing and retrieving university-related information.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UserService userService;
    private final UniversityInfoService universityInfoService;
    private final UniversityInfoRepository universityInfoRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

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
     * @see FullUserResponseDTO
     * @see FullUniversityResponseDTO
     */
    @Override
    public UniversityFullResponseDTO getUniversityFullResponseById(Integer id) throws ResourceNotFoundException {
        return new UniversityFullResponseDTO(
                userService.mappingResponse(userRepository.findUserById(id)),
                modelMapper.map(this.findById(id), FullUniversityResponseDTO.class));
    }

    @Override
    public UniversityFullResponseDTO getSelfProfile() throws ResourceNotFoundException {
        Integer uniId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return new UniversityFullResponseDTO(
                userService.mappingResponse(userRepository.findUserById(uniId)),
                modelMapper.map(this.findById(uniId), FullUniversityResponseDTO.class));
    }

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
    @Override
    public UniversityInfoResponseDTO getUniversityInfoResponseById(Integer id) throws ResourceNotFoundException{
        return new UniversityInfoResponseDTO(
                modelMapper.map(userService.findById(id), InfoUserResponseDTO.class),
                modelMapper.map(this.findById(id), InfoUniversityResponseDTO.class)
        );
    }

    public List<UniversityFullResponseDTO> getUniversityFullResponseList(List<Integer> ids) throws ResourceNotFoundException {
        List<UniversityInfo> universityInfoList = universityInfoRepository.findAllById(ids);

        List<FullUserResponseDTO> fullUserResponseDTOS = userService.getFullUserResponseDTOList(ids);

        return null;
    }

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
    @Override
    public UniversityInfo findById(Integer id) throws ResourceNotFoundException{
        return universityInfoRepository.findById(id).orElseThrow( () -> {
            log.error("University's information with id: {} not found.", id);
            return new ResourceNotFoundException("Thông tin trường học với id: " + id + " không tìm thấy");
        });
    }

    public List<UniversityInfo> findByIds(List<Integer> ids) throws ResourceNotFoundException{
        return universityInfoRepository.findAllById(ids);
    }


    @Transactional
    public UniversityInfo saveUniversityInfo(UniversityInfo universityInfo) throws StoreDataFailedException{
        try {
            universityInfo = universityInfoRepository.save(universityInfo);
            if (universityInfo == null)
                throw new Exception();
        } catch (Exception e){
            throw new StoreDataFailedException("Lưu thông tin trường đại học thất bại");
        }
        return universityInfo;
    }

    public ResponseData updateUniversityStatus(Integer id, AccountStatus status, DeleteUniversityRequest request) throws ResourceNotFoundException, StoreDataFailedException {
        Integer adminId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        User uniAccount = userService.findById(id);

        if (uniAccount.getStatus().equals(status))
            return ResponseData.ok("Trạng thái trường đại học không đúng.");

        if (!uniAccount.getRole().equals(Role.UNIVERSITY))
            throw new ResourceNotFoundException("Không tồn tại trường đại học với id: " + id);
        uniAccount.setStatus(status);
        uniAccount.setNote(request.note());
        uniAccount.setUpdateBy(adminId);
        uniAccount.setUpdateTime(new Date());

        try {
            userRepository.save(uniAccount);
        } catch (Exception e){
            throw new StoreDataFailedException("Cập nhập trạng thái trường đại học thất bại.");
        }

        return ResponseData.ok("Cập nhập trạng thái trường đại học thành công");
    }

    public UniversityFullResponseDTO getUniversityFullResponse() {
        User account = userService.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        UniversityInfo info = findById(account.getId());

        FullUniversityResponseDTO fullInfo = modelMapper.map(info, FullUniversityResponseDTO.class);
        return new UniversityFullResponseDTO(userService.mappingResponse(account), fullInfo);
    }

    @Transactional
    public ResponseData<UpdateUniversityInfoResponse> updateUniversityInfo(UpdateUniversityInfoRequest request) throws ResourceNotFoundException, StoreDataFailedException{
        UniversityInfo universityInfo = findById(request.getId());

        universityInfo.setName(request.getName());
        universityInfo.setDescription(request.getDescription());
        universityInfo.setCoverImage(request.getCoverImage());
        universityInfo.setType(UniversityType.valueOf(request.getType()));
        universityInfo.setCode(request.getCode());

        saveUniversityInfo(universityInfo);
        return ResponseData.ok("Cập nhập thông tin trường đại học thành công.",modelMapper.map(universityInfo, UpdateUniversityInfoResponse.class));
    }

    @Override
    public ResponseData updateUniversityStatus(Integer id, String note) throws ResourceNotFoundException, StoreDataFailedException {
        Integer adminId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        User uniAccount = userService.findById(id);

        if (!uniAccount.getRole().equals(Role.UNIVERSITY))
            throw new ResourceNotFoundException("Không tồn tại trường đại học với id: " + id);

        userService.changeStatus(id, note, "trường học");

        return ResponseData.ok("Cập nhập trạng thái trường đại học thành công");
    }

    public ResponseData<Page<UniversityFullResponseDTO>> getUniversityFullResponseByStaffId(Pageable pageable,
                                                                                            Integer id,
                                                                                            String code,
                                                                                            String username,
                                                                                            String name,
                                                                                            String phone,
                                                                                            String email,
                                                                                            AccountStatus status) {
        Integer staffId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Page<User> uniAccounts = userRepository.findUniversityAccountBy(pageable, id, code, username, name, phone, email, (status != null) ? status.name() : null , staffId);
//        Page<User> uniAccounts = userRepository.findUniversityAccountBy(pageable, null, null, null, null, null, (status != null) ? status.name() : null , staffId);


        if (uniAccounts.getTotalElements() == 0)
            return ResponseData.ok("Không có trường đại học phụ thuộc");

        List<UniversityInfo> uniInfos = this.findByIds(uniAccounts.getContent().stream().map(User::getId).toList());

        List<ActionerDTO> actionerDTOS = userService.getActioners(uniAccounts.stream()
                .flatMap(user -> Stream.of(user.getCreateBy(), user.getUpdateBy()))
                .toList());

        Page<UniversityFullResponseDTO> result = new PageImpl<>(mapping(uniAccounts.getContent(), actionerDTOS, uniInfos), pageable, uniAccounts.getSize());

        return ResponseData.ok("Lấy thông tin các trường đại học phụ thuộc nhân viên thành công.",result);
    }

    public ResponseData<Page<UniversityFullResponseDTO>> getAllUniversityFullResponses(Pageable pageable,
                                                                                       Integer id,
                                                                                       String code,
                                                                                       String username,
                                                                                       String name,
                                                                                       String phone,
                                                                                       String email,
                                                                                       AccountStatus status,
                                                                                       Integer createBy) {
        Page<User> uniAccounts = userRepository.findUniversityAccountBy(pageable, id, code, username, name, phone, email, (status != null) ? status.name() : null , createBy);

        if (uniAccounts.getTotalElements() == 0)
            return ResponseData.ok("Không tìm thấy trường đại học.");

        List<UniversityInfo> uniInfos = this.findByIds(uniAccounts.getContent().stream().map(User::getId).toList());

        List<ActionerDTO> actionerDTOS = userService.getActioners(uniAccounts.stream()
                .flatMap(user -> Stream.of(user.getCreateBy(), user.getUpdateBy()))
                .toList());

        Page<UniversityFullResponseDTO> result = new PageImpl<>(mapping(uniAccounts.getContent(), actionerDTOS, uniInfos), pageable, uniAccounts.getSize());

        return ResponseData.ok("Lấy thông tin các trường đại học thành công.",result);
    }

    public List<UniversityFullResponseDTO> mapping(List<User> uniAccounts, List<ActionerDTO> actionerDTOS, List<UniversityInfo> universityInfos) {
        List<UniversityFullResponseDTO> result = new ArrayList<>();
        uniAccounts.forEach( uniAccount -> {
            UniversityInfo universityInfo = universityInfos.stream().filter(info -> info.getId().equals(uniAccount.getId())).findFirst().get();
            FullUniversityResponseDTO uniInfo = modelMapper.map(universityInfo, FullUniversityResponseDTO.class);
            FullUserResponseDTO accountInfo = userService.mappingResponse(uniAccount, actionerDTOS);
                result.add(
                        UniversityFullResponseDTO.builder()
                                .account(accountInfo)
                                .info(uniInfo)
                                .build()
                );
                }
        );
        return result;
    }

    public UniversityFullResponseDTO mapping(User uniAccount, List<ActionerDTO> actionerDTOS, List<UniversityInfo> universityInfos) {
        return UniversityFullResponseDTO.builder()
                        .account(userService.mappingResponse(uniAccount, actionerDTOS))
                        .info(modelMapper.map(universityInfos.stream().filter(info -> info.getId().equals(uniAccount.getId())), FullUniversityResponseDTO.class))
                        .build();
    }

}
