package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.consultant.ConsultantFullResponseDTO;
import com.demo.admissionportal.dto.entity.consultant.ConsultantResponseDTO;
import com.demo.admissionportal.dto.entity.user.UserResponseDTO;
import com.demo.admissionportal.dto.request.CreateConsultantRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.ConsultantInfoService;
import com.demo.admissionportal.service.UniversityInfoService;
import com.demo.admissionportal.service.UniversityService;
import com.demo.admissionportal.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsultantServiceImpl implements ConsultantInfoService {
    private final ConsultantInfoRepository consultantInfoRepository;
    private final ValidationServiceImpl validationServiceImpl;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final ProvinceRepository provinceRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UniversityInfoRepository universityInfoRepository;
    private final UniversityService universityService;
    private final UserService userService;
    private UniversityInfoService universityInfoService;

    /**
     * Retrieves complete details for a consultant using their ID.
     *
     * <p>This method fetches detailed information about a consultant based
     * on the provided ID. It combines user account data with consultant-specific
     * information into a {@link ConsultantFullResponseDTO}.
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * Integer consultantId = 1;
     * try {
     *     ConsultantFullResponseDTO consultantDetails = consultantService.getById(consultantId);
     *     // ... further processing using the consultantDetails
     * } catch (ResourceNotFoundException e) {
     *     // ... handle the exception (log or return an appropriate response)
     * }
     * }
     * </pre>
     *
     * @param id The ID of the consultant to retrieve details for.
     * @return A {@link ConsultantFullResponseDTO} populated with the
     *         consultant's account information and consultant details.
     * @throws ResourceNotFoundException If no consultant is found with
     *                                    the provided ID.
     *
     * @see ConsultantFullResponseDTO
     * @see UserResponseDTO
     * @see ConsultantResponseDTO
     */
    @Override
    public ConsultantFullResponseDTO getById(Integer id) throws ResourceNotFoundException {
        return new ConsultantFullResponseDTO(
                generateAccountResponse(userService.findById(id)),
                generateConsultantResponse(findById(id))
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

        log.info("Check if username, email, phone are available or not.");
        validationServiceImpl.validateRegister(request.getUsername(), request.getEmail(), request.getPhone());

        Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        log.info("Get current university account id: {}", universityId);

        log.info("Storing consultant's account.");
        User consultant = userRepository.save(new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()), Role.CONSULTANT, universityId));
        if (consultant == null){
            log.error("Store consultant's account failed!");
            throw new StoreDataFailedException("Lưu tài khoản tư vấn viên thất bại.");
        }
        log.info("Store consultant's account succeed.");

        log.info("Get province, district, ward data.");
        Optional<Province> province = provinceRepository.findById(request.getProvinceId());
        Optional<District> district = districtRepository.findById(request.getDistrictId());
        Optional<Ward> ward = wardRepository.findById(request.getWardId());

        if (Stream.of(province, district, ward).anyMatch(Optional::isEmpty)){
            log.error("Province, district, ward was not found");
            throw new ResourceNotFoundException("Dữ liệu địa chỉ không tìm thấy");
        }
        log.info("Get province, district, ward data succeed.");

        log.info("Storing consultant's information.");
        ConsultantInfo consultantInfo = consultantInfoRepository.save(new ConsultantInfo(
                consultant.getId(),
                universityId,
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                request.getPhone(),
                request.getSpecificAddress(),
                Gender.valueOf(request.getGender()),
                province.get(),
                district.get(),
                ward.get()));
        if (consultantInfo == null) {
            log.error("Storing consultant's information failed.");
            throw new StoreDataFailedException("Lưu thông tin tư vấn viên thất bại.");
        }
        log.info("Storing consultant's information succeed.");

        return ResponseData.created("Tạo tư vấn viên thành công.");
    }

    /**
     * Generates a {@link UserResponseDTO} representation of a {@link User} object,
     * including information about the creator and last updater.
     *
     * <p> This method maps basic user details from a {@link User} object
     * to a {@link UserResponseDTO}. Additionally, it retrieves details of the
     * creating and updating users (using {@link UserService#findById} and maps
     * them to {@link ActionerDTO} objects within the response.
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * User user = // ... retrieve a User object ...
     * UserResponseDTO responseDTO = generateAccountResponse(user);
     * // ... further processing using the populated responseDTO
     * }
     * </pre>
     *
     * @param account The {@link User} object used to populate the response DTO.
     * @return A {@link UserResponseDTO} containing mapped user data,
     *         as well as creator and updater details as {@link ActionerDTO} objects.
     *
     * @see User
     * @see UserResponseDTO
     * @see ActionerDTO
     * @see UserService
     */
    protected UserResponseDTO generateAccountResponse(User account){
        UserResponseDTO accountResponse = modelMapper.map(account, UserResponseDTO.class);
        accountResponse.setCreateBy(modelMapper.map(userService.findById(account.getCreateBy()), ActionerDTO.class));
        if (account.getUpdateBy() == null)
            accountResponse.setUpdateBy(null);
        else
            accountResponse.setUpdateBy(modelMapper.map(userService.findById(account.getUpdateBy()), ActionerDTO.class));
        return accountResponse;
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
    protected ConsultantResponseDTO generateConsultantResponse(ConsultantInfo info){
        ConsultantResponseDTO infoResponse = modelMapper.map(info, ConsultantResponseDTO.class);
        infoResponse.setUniversity(universityService.getUniversityInfoResponseById(info.getUniversityId()));
        infoResponse.convertName(info);
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
    public ConsultantInfo findById(Integer id) throws ResourceNotFoundException{
        return consultantInfoRepository.findById(id).orElseThrow(() -> {
            log.error("Consultant's information with id: {} not found.", id);
            return new ResourceNotFoundException("University's information with id: " + id + " not found");
        });
    }

}
