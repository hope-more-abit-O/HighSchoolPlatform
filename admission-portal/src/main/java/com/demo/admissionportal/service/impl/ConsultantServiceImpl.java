package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.consultant.FullConsultantResponseDTO;
import com.demo.admissionportal.dto.entity.consultant.ConsultantResponseDTO;
import com.demo.admissionportal.dto.entity.consultant.InfoConsultantResponseDTO;
import com.demo.admissionportal.dto.entity.user.FullUserResponseDTO;
import com.demo.admissionportal.dto.request.consultant.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.consultant.ChangeConsultantStatusRequest;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.NotAllowedException;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.ConsultantService;
import com.demo.admissionportal.service.UniversityService;
import com.demo.admissionportal.service.UserService;
import com.demo.admissionportal.service.ValidationService;
import com.demo.admissionportal.util.impl.AddressUtils;
import com.demo.admissionportal.util.impl.EmailUtil;
import com.demo.admissionportal.util.impl.NameUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsultantServiceImpl implements ConsultantService {
    private final ConsultantInfoRepository consultantInfoRepository;
    private final ValidationService validationService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UniversityService universityService;
    private final UserService userService;
    private final AddressServiceImpl addressServiceImpl;
    private final DistrictServiceImpl districtServiceImpl;
    private final EmailUtil emailUtil;
    private final WardServiceImpl wardServiceImpl;

    /**
     * Retrieves complete details for a consultant using their ID.
     *
     * <p>This method fetches detailed information about a consultant based
     * on the provided ID. It combines user account data with consultant-specific
     * information into a {@link FullConsultantResponseDTO}.
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * Integer consultantId = 1;
     * try {
     *     ConsultantFullResponseDTO consultantDetails = consultantService.getFullConsultantById(consultantId);
     *     // ... further processing using the consultantDetails
     * } catch (ResourceNotFoundException e) {
     *     // ... handle the exception (log or return an appropriate response)
     * }
     * }
     * </pre>
     *
     * @param id The ID of the consultant to retrieve details for.
     * @return A {@link FullConsultantResponseDTO} populated with the
     *         consultant's account information and consultant details.
     * @throws ResourceNotFoundException If no consultant is found with
     *                                    the provided ID.
     *
     * @see FullConsultantResponseDTO
     * @see FullUserResponseDTO
     * @see ConsultantResponseDTO
     */
    @Override
    public FullConsultantResponseDTO getFullConsultantById(Integer id) throws ResourceNotFoundException {
        return new FullConsultantResponseDTO(
                modelMapper.map(userService.findById(id), FullUserResponseDTO.class),
                mappingResponse(findInfoById(id))
        );
    }

    /**
     * Creates a new consultant account and associated information.
     *
     * <p> This method processes a {@link CreateConsultantRequest}, validates
     * the request data, creates a user account for the consultant (with the
     * `CONSULTANT` role), and stores their personal information. It retrieves
     *  the university ID from the currently authenticated user's context.
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * CreateConsultantRequest request = new CreateConsultantRequest();
     * // ... populate the request object with consultant details ...
     *
     * try {
     *     ResponseData response = consultantService.createConsultant(request);
     *     // ... handle success (e.g., return response to the client)
     * } catch (DataExistedException | StoreDataFailedException | ResourceNotFoundException e) {
     *     // ... handle exceptions (log the error or return an error response)
     * }
     * }
     * </pre>
     *
     * @param request  The {@link CreateConsultantRequest} object containing
     *                 the new consultant's details.
     * @return A {@link ResponseData} indicating successful creation of
     *         the consultant account.
     * @throws DataExistedException      If the username, email, or phone
     *                                    number already exists.
     * @throws StoreDataFailedException If there is an error saving the
     *                                    consultant's account or information.
     * @throws ResourceNotFoundException If province, district, or ward data is not found.
     *
     * @see CreateConsultantRequest
     * @see ResponseData
     * @see User
     * @see ConsultantInfo
     */
    @Override
    @Transactional
    public ResponseData createConsultant(CreateConsultantRequest request) throws DataExistedException,StoreDataFailedException,ResourceNotFoundException {
        log.info("Trimming request data");
        request.trim();

        String password = RandomStringUtils.randomAlphanumeric(9);

        log.info("Check if username, email, phone are available or not.");
        validationService.validateRegister(request.getUsername(), request.getEmail(), request.getPhone());

        Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        log.info("Get current university account id: {}", universityId);

        log.info("Storing consultant's account.");
        User consultant = userRepository.save(new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(password), Role.CONSULTANT, universityId));
        if (consultant == null){
            log.error("Store consultant's account failed!");
            throw new StoreDataFailedException("Lưu tài khoản tư vấn viên thất bại.");
        }
        log.info("Store consultant's account succeed.");

        log.info("Storing consultant's information.");
        ConsultantInfo consultantInfo = consultantInfoRepository.save(new ConsultantInfo(
                consultant.getId(),
                universityId,
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                request.getPhone(),
                Gender.MALE
        ));
        if (consultantInfo == null) {
            log.error("Storing consultant's information failed.");
            throw new StoreDataFailedException("Lưu thông tin tư vấn viên thất bại.");
        }
        log.info("Storing consultant's information succeed.");


        emailUtil.sendAccountPasswordRegister(consultant, password);
        return ResponseData.created("Tạo tư vấn viên thành công.");
    }

    /**
     * Generates a {@link ConsultantResponseDTO} from a {@link ConsultantInfo} object.
     *
     * <p> This method maps the base information from the {@link ConsultantInfo} object
     * to a {@link ConsultantResponseDTO}. Additionally, it retrieves and sets
     * university details using the {@link UniversityService} and converts the
     * consultant's name using the {@link ConsultantResponseDTO#convertName} method.
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * ConsultantInfo consultantInfo = // ... retrieve a ConsultantInfo object
     * ConsultantResponseDTO responseDTO = generateConsultantResponse(consultantInfo);
     * // ... further processing using the populated responseDTO
     * }
     * </pre>
     *
     * @param info The {@link ConsultantInfo} object used as the source for data.
     * @return A fully populated {@link ConsultantResponseDTO} containing
     *         information from the provided {@link ConsultantInfo} object, university details,
     *         and a converted name.
     *
     * @see ConsultantInfo
     * @see ConsultantResponseDTO
     * @see UniversityService
     */
    protected ConsultantResponseDTO mappingResponse(ConsultantInfo info){
        ConsultantResponseDTO infoResponse = modelMapper.map(info, ConsultantResponseDTO.class);
        infoResponse.setName(NameUtils.getFullName(info.getFirstname(), info.getMiddleName(), info.getLastName()));
        infoResponse.setUniversity(universityService.getUniversityInfoResponseById(info.getUniversityId()));
        if (info.getSpecificAddress() == null || info.getProvince() == null || info.getWard() == null || info.getDistrict() == null){
            infoResponse.setAddress(null);
        } else
            infoResponse.setAddress(AddressUtils.getFullAddress(info.getSpecificAddress(), info.getWard(), info.getDistrict(), info.getProvince()));

        return infoResponse;
    }

    /**
     * Retrieves a {@link ConsultantInfo} entity by its ID.
     *
     * <p>This method attempts to find a consultant information record with the specified `id`.
     * If a matching record is found, it's returned. Otherwise, a {@link ResourceNotFoundException} is thrown.
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * Integer consultantId = 123;
     * try {
     *     ConsultantInfo consultantInfo = consultantInfoService.findById(consultantId);
     *     // ... process the retrieved consultantInfo
     * } catch (ResourceNotFoundException e) {
     *     // ... handle the exception, such as logging or returning an error response
     * }
     * }
     * </pre>
     *
     * @param id The ID of the consultant information to retrieve.
     * @return The {@link ConsultantInfo} entity if found.
     * @throws ResourceNotFoundException If no consultant information with the given ID is found.
     *
     * @see ConsultantInfo
     * @see ResourceNotFoundException
     */
    public ConsultantInfo findInfoById(Integer id) throws ResourceNotFoundException{
        return consultantInfoRepository.findById(id).orElseThrow(() -> {
            log.error("Consultant's information with id: {} not found.", id);
            return new ResourceNotFoundException("Tư vấn viên với Id: " + id + " không tìm tấy");
        });
    }

    public ResponseData selfUpdateConsultantInfo(SelfUpdateConsultantInfoRequest request)
            throws ResourceNotFoundException, StoreDataFailedException {
        Integer consultantId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        userService.updateUser(consultantId, request.getUsername(), request.getEmail(), consultantId, "tư vấn viên");

        ConsultantInfo consultantInfo = findInfoById(consultantId);
        updateConsultantInfo(consultantInfo, request);

        ConsultantResponseDTO response = mappingResponse(consultantInfo);
        return ResponseData.ok("Cập nhật thông tin tư vấn viên thành công.", response);
    }

    public ResponseData selfUpdateConsultantAddress(UpdateConsultantAddressRequest request)
            throws ResourceNotFoundException, StoreDataFailedException {
        Integer consultantId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ConsultantInfo consultantInfo = findInfoById(consultantId);
        updateConsultantAddress(consultantInfo, request);

        ConsultantResponseDTO response = mappingResponse(consultantInfo);
        return ResponseData.ok("Cập nhật thông tin tư vấn viên thành công.", response);
    }

    public ResponseData updateConsultantInfoById(UpdateConsultantInfoByIdRequest request)
            throws ResourceNotFoundException, StoreDataFailedException {
        ConsultantInfo consultantInfo = findInfoById(request.getId());
        updateConsultantInfo(consultantInfo, request);

        ConsultantResponseDTO response = mappingResponse(consultantInfo);
        return ResponseData.ok("Cập nhật thông tin tư vấn viên thành công.", response);
    }

    private void updateConsultantInfo(ConsultantInfo consultantInfo, ConsultantInfoRequest request)
            throws StoreDataFailedException {
        validationService.validatePhoneNumber(request.getPhone());
        consultantInfo.setFirstname(request.getFirstName());
        consultantInfo.setMiddleName(request.getMiddleName());
        consultantInfo.setLastName(request.getLastName());
        consultantInfo.setPhone(request.getPhone());
        consultantInfo.setBirthday(Date.valueOf(request.getBirthDate()));

        save(consultantInfo);
    }

    private void updateConsultantAddress(ConsultantInfo consultantInfo, UpdateConsultantAddressRequest request)
            throws StoreDataFailedException, ResourceNotFoundException {
        consultantInfo.setSpecificAddress(request.getSpecificAddress());
        consultantInfo.setProvince(addressServiceImpl.findById(request.getProvinceId()));
        consultantInfo.setWard(wardServiceImpl.findById(request.getWardId()));
        consultantInfo.setDistrict(districtServiceImpl.findById(request.getDistrictId()));

        save(consultantInfo);
    }

    public ConsultantInfo save(ConsultantInfo consultantInfo) throws StoreDataFailedException{
        try {
            if (consultantInfoRepository.save(consultantInfo) == null)
                throw new Exception();
        } catch (Exception e) {
            throw new StoreDataFailedException("Lưu thông tin tư vấn viên thất bại");
        }
        return consultantInfo;
    }

    public ResponseData updateConsultantStatus(Integer id, ChangeConsultantStatusRequest request) throws NotAllowedException, ResourceNotFoundException, BadRequestException, StoreDataFailedException {
        userService.changeConsultantStatus(id, request.getNote());
        return ResponseData.ok("Cập nhập tư vấn viên thành công");
    }

    public InfoConsultantResponseDTO findById(Integer id) throws ResourceNotFoundException {
        User account = userService.findById(id);

        ConsultantInfo consultantInfo = findInfoById(id);

        InfoConsultantResponseDTO response = new InfoConsultantResponseDTO();

        return null;
    }
}