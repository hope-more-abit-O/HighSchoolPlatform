package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.consultant.ConsultantInfoResponseDTO;
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
import com.demo.admissionportal.service.*;
import com.demo.admissionportal.util.impl.AddressUtils;
import com.demo.admissionportal.util.impl.EmailUtil;
import com.demo.admissionportal.util.impl.NameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.stream.Streams;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
    private final AddressService addressService;
    private final DistrictServiceImpl districtService;
    private final EmailUtil emailUtil;
    private final WardServiceImpl wardService;

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
    public FullConsultantResponseDTO getFullConsultantByIdByUniversity(Integer id) throws ResourceNotFoundException {
        Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User consultantAccount = userService.findById(id);

        if (!consultantAccount.getCreateBy().equals(universityId))
            throw new NotAllowedException("Bạn không có quyền để xem thông tin của tư vấn viên này");

        return new FullConsultantResponseDTO(
                userService.mappingResponse(consultantAccount),
                mappingResponse(findInfoById(id))
        );
    }

    @Override
    public FullConsultantResponseDTO getSelfInfo() throws ResourceNotFoundException {
        Integer consultantId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return new FullConsultantResponseDTO(
                userService.mappingResponse(userService.findById(consultantId)),
                mappingResponse(findInfoById(consultantId))
        );
    }

    @Override
    public FullConsultantResponseDTO getFullConsultantById(Integer id) throws ResourceNotFoundException {
        return new FullConsultantResponseDTO(
                modelMapper.map(userService.findById(id), FullUserResponseDTO.class),
                mappingResponse(findInfoById(id))
        );
    }

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
        log.info("Password: {}", password);


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

    protected ConsultantResponseDTO mappingResponse(ConsultantInfo info){
        ConsultantResponseDTO infoResponse = modelMapper.map(info, ConsultantResponseDTO.class);

        infoResponse.setName(NameUtils.getFullName(info.getFirstName(), info.getMiddleName(), info.getLastName()));
        infoResponse.setUniversity(universityService.getUniversityInfoResponseById(info.getUniversityId()));

        if (info.getSpecificAddress() == null || info.getProvince() == null || info.getWard() == null || info.getDistrict() == null){
            infoResponse.setAddress(null);
        } else
            infoResponse.setAddress(AddressUtils.getFullAddress(info.getSpecificAddress(), info.getWard(), info.getDistrict(), info.getProvince()));

        return infoResponse;
    }

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
        consultantInfo.setFirstName(request.getFirstName());
        consultantInfo.setMiddleName(request.getMiddleName());
        consultantInfo.setLastName(request.getLastName());
        consultantInfo.setPhone(request.getPhone());
        consultantInfo.setBirthday(Date.valueOf(request.getBirthDate()));

        save(consultantInfo);
    }

    private void updateConsultantAddress(ConsultantInfo consultantInfo, UpdateConsultantAddressRequest request)
            throws StoreDataFailedException, ResourceNotFoundException {
        consultantInfo.setSpecificAddress(request.getSpecificAddress());
        consultantInfo.setProvince(addressService.findProvinceById(request.getProvinceId()));
        consultantInfo.setWard(wardService.findById(request.getWardId()));
        consultantInfo.setDistrict(districtService.findById(request.getDistrictId()));

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
        User consultantAccount = userService.changeConsultantStatus(id, request.getNote());
        return ResponseData.ok("Cập nhập tư vấn viên thành công", userService.mappingResponse(consultantAccount));
    }

    public InfoConsultantResponseDTO getById(Integer id) throws ResourceNotFoundException {
        User account = userService.findById(id);

        ConsultantInfo consultantInfo = findInfoById(id);

        InfoConsultantResponseDTO response = new InfoConsultantResponseDTO();

        return null;
    }

    public Page<FullConsultantResponseDTO> getConsultant(Integer createBy, Pageable pageable) throws ResourceNotFoundException {
        List<User> consultantAccounts = userService.findByCreateByAndRole(createBy, Role.CONSULTANT, pageable).getContent();
        List<Integer> actionerIds = Streams.nonNull(consultantAccounts.stream().flatMap(user -> Stream.of(user.getCreateBy(), user.getUpdateBy()))).toList();
        List<ConsultantInfo> infos = consultantInfoRepository.findAllById(consultantAccounts.stream().map(User::getId).distinct().toList());
        List<ActionerDTO> actionerDTOS = userService.getActioners(actionerIds);

        List<FullConsultantResponseDTO> fullConsultantResponseDTOS = new ArrayList<>();

        for (User consultantAccount : consultantAccounts) {
            fullConsultantResponseDTOS.add(FullConsultantResponseDTO.builder()
                    .account(userService.mappingResponse(consultantAccount, actionerDTOS))
                    .info(mappingResponse(infos.stream().filter(info -> info.getId().equals(consultantAccount.getId())).findFirst().get()))
                    .build());
        }

        return new PageImpl<>(fullConsultantResponseDTOS, pageable, infos.size());
    }
}