package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.IdAndName;
import com.demo.admissionportal.dto.entity.user.FullUserResponseDTO;
import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import com.demo.admissionportal.dto.request.ChangeStatusUserRequestDTO;
import com.demo.admissionportal.dto.request.UpdateUserRequestDTO;
import com.demo.admissionportal.dto.response.*;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.exception.exceptions.NotAllowedException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.UserService;
import com.demo.admissionportal.service.ValidationService;
import com.demo.admissionportal.util.impl.NameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type User service.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final ModelMapper modelMapper;
    private final ValidationService validationService;
    private final UniversityInfoServiceImpl universityInfoServiceImpl;
    private final ConsultantInfoServiceImpl consultantInfoService;
    private final StaffInfoServiceImpl staffInfoService;


    @Override
    public ResponseData<Page<UserResponseDTO>> getUser(String username, String firstName, String middleName, String lastName, String phone, String email,
                                                       String specificAddress, String educationLevel, String status, Pageable pageable) {
        try {
            List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
            Page<UserInfo> userPage = userInfoRepository.findAll(username, firstName, middleName, lastName, phone, email, specificAddress, educationLevel, status, pageable);
            // Map UserInfo to UserResponseDTO
            userPage.forEach(userInfo -> {
                UserResponseDTO responseDTO = new UserResponseDTO();
                responseDTO.setId(userInfo.getId());
                responseDTO.setUsername(userInfo.getUser().getUsername());
                responseDTO.setEmail(userInfo.getUser().getEmail());
                responseDTO.setName(userInfo.getFirstName() + " " + userInfo.getMiddleName() + " " + userInfo.getLastName());
                responseDTO.setStatus(userInfo.getUser().getStatus().name);
                responseDTO.setCreate_time(userInfo.getUser().getCreateTime());
                responseDTO.setNote(userInfo.getUser().getNote());
                userResponseDTOS.add(responseDTO);
            });

            Page<UserResponseDTO> result = new PageImpl<>(userResponseDTOS, pageable, userPage.getTotalElements());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy danh sách user", result);
        } catch (Exception ex) {
            log.error("Error while getting list user: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi tìm user");
        }
    }

    @Override
    public ResponseData<UserProfileResponseDTO> getUserById(Integer id) {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (!Objects.equals(id, userId)) {
                return new ResponseData<>(ResponseCode.C209.getCode(), "Không đúng user");
            }
            if (id == null || id < 0) {
                new ResponseEntity<ResponseData<User>>(HttpStatus.BAD_REQUEST);
            }
            UserInfo userInfo = userInfoRepository.findUserInfoById(id);
            User user = userRepository.findUserById(id);
            if (userInfo == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");
            }
            Ward ward = wardRepository.findWardById(userInfo.getWard().getId());
            District district = districtRepository.findDistrictById(userInfo.getDistrict().getId());
            Province province = provinceRepository.findProvinceById(userInfo.getProvince().getId());

            UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO();
            userProfileResponseDTO.setId(user.getId());
            userProfileResponseDTO.setEmail(user.getEmail());
            userProfileResponseDTO.setUsername(user.getUsername());
            userProfileResponseDTO.setFirstname(userInfo.getFirstName());
            userProfileResponseDTO.setMiddle_name(userInfo.getMiddleName());
            userProfileResponseDTO.setLastname(userInfo.getLastName());
            userProfileResponseDTO.setGender(userInfo.getGender());

            // Convert dd-MM-YYYY
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String dateUserProfile = formatter.format(date);
            userProfileResponseDTO.setBirthday(dateUserProfile);

            userProfileResponseDTO.setPhone(userInfo.getPhone());
            userProfileResponseDTO.setSpecificAddress(userInfo.getSpecificAddress());
            userProfileResponseDTO.setEducation_level(userInfo.getEducationLevel());
            userProfileResponseDTO.setWard(ward);
            userProfileResponseDTO.setDistrict(district);
            userProfileResponseDTO.setProvince(province);
            userProfileResponseDTO.setAvatar(user.getAvatar());

            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy user", userProfileResponseDTO);
        } catch (Exception ex) {
            log.error("Error while getting user: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi tìm user");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public ResponseData<UpdateUserResponseDTO> updateUser(Integer id, UpdateUserRequestDTO requestDTO) {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (id == null || id < 0 || requestDTO == null) {
                new ResponseEntity<ResponseData<UpdateUserResponseDTO>>(HttpStatus.BAD_REQUEST);
            }
            if (!Objects.equals(id, userId)) {
                return new ResponseData<>(ResponseCode.C209.getCode(), "Không đúng user");
            }
            UserInfo userInfo = userInfoRepository.findUserInfoById(id);
            User user = userRepository.findUserById(id);
            // Get user profile
            if (userInfo == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");
            }
            // Update profile
            userInfo.setFirstName(requestDTO.getFirstName());
            userInfo.setMiddleName(requestDTO.getMiddleName());
            userInfo.setLastName(requestDTO.getLastName());
            userInfo.setGender(requestDTO.getGender());
            validationService.validatePhoneNumber(requestDTO.getPhone());
            userInfo.setPhone(requestDTO.getPhone());

            userInfo.setBirthday(requestDTO.getBirthday());

            Ward ward = wardRepository.findWardById(requestDTO.getWard());
            District district = districtRepository.findDistrictById(requestDTO.getDistrict());
            Province province = provinceRepository.findProvinceById(requestDTO.getProvince());

            userInfo.setProvince(province);
            userInfo.setDistrict(district);
            userInfo.setWard(ward);
            userInfo.setEducationLevel(requestDTO.getEducation_level());
            userInfo.setPhone(requestDTO.getPhone());
            userInfo.setSpecificAddress(requestDTO.getSpecific_address());
            user.setAvatar(requestDTO.getAvatar());

            // Save update time
            user.setUpdateTime(new Date());

            userRepository.save(user);
            userInfoRepository.save(userInfo);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã cập nhật user thành công");
        } catch (DataExistedException de) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoai đã tồn tại");

        } catch (Exception ex) {
            log.error("Error while update user: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi cập nhật user");
        }
    }

    @Override
    public ResponseData<ChangeStatusUserResponseDTO> changeStatus(Integer id, ChangeStatusUserRequestDTO requestDTO) {
        try {
            if (id == null || id < 0 || requestDTO == null) {
                new ResponseEntity<ResponseData<User>>(HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findUserById(id);
            if (user == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");
            }
            if (user.getStatus().equals(AccountStatus.ACTIVE)) {
                user.setNote(requestDTO.getNote());
                user.setStatus(AccountStatus.INACTIVE);
            } else {
                user.setNote(requestDTO.getNote());
                user.setStatus(AccountStatus.ACTIVE);
            }
            userRepository.save(user);
            ChangeStatusUserResponseDTO response = new ChangeStatusUserResponseDTO();
            response.setCurrentStatus(user.getStatus().name);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã cập nhật trạng thái thành công", response);
        } catch (Exception ex) {
            log.error("Error while change status user: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi thay đổi trạng thái user");
        }
    }

    @Override
    public User findById(Integer id) throws ResourceNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("User's account with id: {} not found.", id);
            return new ResourceNotFoundException("Tài khoản với id: " + id + " không tìm thấy");
        });
    }


    @Override
    public List<User> findByIds(List<Integer> ids) {
        return userRepository.findByIdIn(ids);
    }

    @Override
    public FullUserResponseDTO mappingFullResponse(User user) throws ResourceNotFoundException {
        FullUserResponseDTO responseDTO = modelMapper.map(user, FullUserResponseDTO.class);
        ActionerDTO actionerDTO = modelMapper.map(findById(user.getCreateBy()), ActionerDTO.class);
        responseDTO.setCreateBy(actionerDTO);

        if (user.getUpdateBy() == null) //Case 1: updateBy == null
            responseDTO.setUpdateBy(null);
        else if (Objects.equals(user.getCreateBy(), user.getUpdateBy())) //Case 2: updateBy == createBy
            responseDTO.setUpdateBy(actionerDTO);
        else //Case 3: updateBy != createBy
            responseDTO.setUpdateBy(modelMapper.map(findById(user.getUpdateBy()), ActionerDTO.class));

        return responseDTO;
    }

    @Override
    public FullUserResponseDTO mappingFullResponse(User user, List<ActionerDTO> actionerDTOs) throws ResourceNotFoundException {

        ActionerDTO createBy = actionerDTOs
                .stream()
                .filter(actioner -> actioner.getId().equals(user.getCreateBy()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy actioner trong list"));

        FullUserResponseDTO result = modelMapper.map(user, FullUserResponseDTO.class);
        result.setCreateBy(createBy);

        if (user.getUpdateBy() != null) {
            ActionerDTO updateBy = actionerDTOs
                    .stream()
                    .filter(actioner -> actioner.getId().equals(user.getUpdateBy()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy actioner trong list"));
            result.setUpdateBy(updateBy);
        } else result.setUpdateBy(null);

        return result;
    }

    @Override
    public InfoUserResponseDTO mappingInfoResponse(User user) throws ResourceNotFoundException {
        return modelMapper.map(user, InfoUserResponseDTO.class);
    }

    public List<FullUserResponseDTO> mappingFullResponse(List<User> users) throws ResourceNotFoundException {
        List<FullUserResponseDTO> responseDTOs = new ArrayList<>();

        List<ActionerDTO> actionerDTOS = this.getActioners(users.stream()
                .flatMap(user -> Stream.of(user.getCreateBy(), user.getUpdateBy()))
                .toList());

        for (User user : users) {
            FullUserResponseDTO responseDTO = modelMapper.map(user, FullUserResponseDTO.class);
            ActionerDTO actionerDTO = modelMapper.map(findById(user.getCreateBy()), ActionerDTO.class);
            responseDTO.setCreateBy(actionerDTO);

            responseDTOs.add(responseDTO);

            if (user.getUpdateBy() == null) //Case 1: updateBy == null
                responseDTO.setUpdateBy(null);
            else if (Objects.equals(user.getCreateBy(), user.getUpdateBy())) //Case 2: updateBy == createBy
                responseDTO.setUpdateBy(actionerDTO);
            else //Case 3: updateBy != createBy
                responseDTO.setUpdateBy(modelMapper.map(findById(user.getUpdateBy()), ActionerDTO.class));
        }

        return responseDTOs;
    }

    public List<ActionerDTO> mapActioners(Object obj) throws ResourceNotFoundException {
        List<ActionerDTO> actionerDTOList = new ArrayList<>();

        // Use reflection to get the values of createBy and updateBy
        try {
            // Get the createBy and updateBy methods
            java.lang.reflect.Method getCreateByMethod = obj.getClass().getMethod("getCreateBy");
            Method getUpdateByMethod = obj.getClass().getMethod("getUpdateBy");

            // Invoke the methods to get the values
            Object createByValue = getCreateByMethod.invoke(obj);
            Object updateByValue = getUpdateByMethod.invoke(obj);

            // Map CreateBy field
            ActionerDTO createByDTO = modelMapper.map(findById((Integer) createByValue), ActionerDTO.class);
            actionerDTOList.add(createByDTO);

            // Map UpdateBy field
            if (updateByValue == null) { // Case 1: updateBy == null
                actionerDTOList.add(null);
            } else if (Objects.equals(createByValue, updateByValue)) { // Case 2: updateBy == createBy
                actionerDTOList.add(createByDTO);
            } else { // Case 3: updateBy != createBy
                ActionerDTO updateByDTO = modelMapper.map(findById((Integer) updateByValue), ActionerDTO.class);
                actionerDTOList.add(updateByDTO);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ResourceNotFoundException("Error mapping actioners", Map.of("error", e.getMessage()));
        }

        return actionerDTOList;
    }

    public ActionerDTO getActionerDTOById(Integer id) throws ResourceNotFoundException {
        User user = findById(id);
        return modelMapper.map(user, ActionerDTO.class);
    }

    public List<ActionerDTO> getActionerDTOsByIds(List<Integer> id) throws ResourceNotFoundException {
        List<User> users = findByIds(id);
        return users.stream()
                .map(user -> modelMapper.map(user, ActionerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user, String name) throws StoreDataFailedException {
        User result;
        try {
            result = userRepository.save(user);
            if (result == null)
                throw new Exception();
        } catch (Exception e) {
            throw new StoreDataFailedException("Lưu " + name + " thất bại.");
        }
        return result;
    }

    public User changeStatus(Integer id, String note, String name) throws StoreDataFailedException, ResourceNotFoundException {
        Integer actionerId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        User account = findById(id);

        if (account.getStatus().equals(AccountStatus.ACTIVE))
            account.setStatus(AccountStatus.INACTIVE);
        else account.setStatus(AccountStatus.ACTIVE);
        account.setNote(note);
        account.setUpdateTime(new Date());
        account.setUpdateBy(actionerId);
        try {
            userRepository.save(account);
        } catch (Exception e) {
            throw new StoreDataFailedException("Cập nhập trạng thái " + name + " thất bại.");
        }
        return account;
    }

    public User changeConsultantStatus(Integer id, String note) throws NotAllowedException, StoreDataFailedException, ResourceNotFoundException {
        Integer actionerId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        User account = findById(id);

        if (!account.getCreateBy().equals(actionerId))
            throw new NotAllowedException("Không thể thực hiện hành động vì tư vấn viên không dưới quyền quản lý.");
        if (account.getStatus().equals(AccountStatus.ACTIVE))
            account.setStatus(AccountStatus.INACTIVE);
        else account.setStatus(AccountStatus.ACTIVE);

        account.setNote(note);
        account.setUpdateTime(new Date());
        account.setUpdateBy(actionerId);
        try {
            account = userRepository.save(account);
        } catch (Exception e) {
            throw new StoreDataFailedException("Cập nhập trạng thái tư vấn viên thất bại.");
        }
        return account;
    }

    public User updateUser(Integer id, String username, String email, Integer updateById, String name) throws StoreDataFailedException {
        User user = findById(id);
        validationService.validateRegister(username, email);

        user.setUsername(username);
        user.setEmail(email);
        user.setUpdateBy(updateById);
        user.setUpdateTime(new Date());

        return save(user, name);
    }

    public InfoUserResponseDTO getInfoUserResponseDTOById(Integer id) throws ResourceNotFoundException {
        return modelMapper.map(findById(id), InfoUserResponseDTO.class);
    }

    public FullUserResponseDTO getFullUserResponseDTOById(Integer id) throws ResourceNotFoundException {
        return modelMapper.map(findById(id), FullUserResponseDTO.class);
    }

    public List<FullUserResponseDTO> getFullUserResponseDTOList(List<Integer> ids) throws ResourceNotFoundException {
        List<User> users = userRepository.findAllById(ids);

        Map<Integer, ActionerDTO> actioners = fetchAndMapActionerUsers(extractActionerIds(users));

        return users.stream()
                .map(user -> mapToFullUserResponseDTO(user, actioners))
                .collect(Collectors.toList());
    }

    public Map<Integer, FullUserResponseDTO> getFullUserResponseDTOMap(List<Integer> ids) throws ResourceNotFoundException {
        List<User> users = userRepository.findAllById(ids);

        Map<Integer, ActionerDTO> actioners = fetchAndMapActionerUsers(extractActionerIds(users));

        return users.stream()
                .map(user -> mapToFullUserResponseDTO(user, actioners))
                .collect(Collectors.toMap(FullUserResponseDTO::getId, dto -> dto));
    }

    public List<InfoUserResponseDTO> getInfoUserResponseDTOList(List<Integer> ids) throws ResourceNotFoundException {
        List<User> users = userRepository.findAllById(ids);

        return users.stream()
                .map(user -> modelMapper.map(user, InfoUserResponseDTO.class))
                .collect(Collectors.toList());
    }

    private Set<Integer> extractActionerIds(List<User> users) {
        return users.stream()
                .flatMap(user -> Stream.of(user.getCreateBy(), user.getUpdateBy()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Map<Integer, ActionerDTO> fetchAndMapActionerUsers(Set<Integer> actionerIds) {
        List<User> actionerUsers = userRepository.findAllById(new ArrayList<>(actionerIds));
        return actionerUsers.stream()
                .collect(Collectors.toMap(
                        User::getId,
//                        user -> new ActionerDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name(), user.getStatus())
                        user -> modelMapper.map(user, ActionerDTO.class)
                ));
    }

    private FullUserResponseDTO mapToFullUserResponseDTO(User user, Map<Integer, ActionerDTO> actionerMap) {
        FullUserResponseDTO fullUserResponseDTO = modelMapper.map(user, FullUserResponseDTO.class);
        fullUserResponseDTO.setCreateBy(actionerMap.get(user.getCreateBy()));
        fullUserResponseDTO.setUpdateBy(actionerMap.get(user.getUpdateBy()));
        return fullUserResponseDTO;
    }

    @Override
    public Page<User> findByCreateByAndRole(Integer id, Role role, Pageable pageable) throws ResourceNotFoundException {
        return userRepository.findByCreateByAndRole(id, role, pageable);
    }

    @Override
    public List<ActionerDTO> getActioners(List<Integer> ids) throws ResourceNotFoundException {
        List<User> users = userRepository.findAllById(ids.stream().distinct().toList());
        if (users.isEmpty())
            throw new ResourceNotFoundException("Không tìm thấy actioner!", Map.of("ids", ids.toString()));
        List<ActionerDTO> result = users.stream().map((element) -> modelMapper.map(element, ActionerDTO.class)).toList();

        List<IdAndName> idAndNames = new ArrayList<>();
        for (User user : users) {
            switch (user.getRole()) {
                case UNIVERSITY:
                    UniversityInfo uniInfo = universityInfoServiceImpl.findById(user.getId());
                    idAndNames.add(new IdAndName(uniInfo.getId(), uniInfo.getName()));
                    break;
                case CONSULTANT:
                    ConsultantInfo consultantInfo = consultantInfoService.findById(user.getId());
                    idAndNames.add(new IdAndName(consultantInfo.getId(), NameUtils.getFullName(consultantInfo.getFirstName(), consultantInfo.getMiddleName(), consultantInfo.getLastName())));
                    break;
                case STAFF:
                    StaffInfo staffInfo = staffInfoService.findStaffInfoById(user.getId());
                    idAndNames.add(new IdAndName(staffInfo.getId(), NameUtils.getFullName(staffInfo.getFirstName(), staffInfo.getMiddleName(), staffInfo.getLastName())));
                    break;
                case USER:
                    UserInfo userInfo = this.findUserInfoById(user.getId());
                    idAndNames.add(new IdAndName(userInfo.getId(), NameUtils.getFullName(userInfo.getFirstName(), userInfo.getMiddleName(), userInfo.getLastName())));
                    break;
            }
        }

        result.forEach(
                rs -> rs.setFullName(idAndNames
                        .stream()
                        .filter(i -> rs.getId().equals(i.getId()))
                        .map(IdAndName::getName)
                        .findFirst()
                        .orElse("ADMIN"))
        );

        return result;
    }

    @Override
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public UserInfo findUserInfoById(Integer id) {
        return userInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Thông tin người dùng không tìm thấy"));
    }

    public Page<User> findByRoleAndPageable(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }

    @Override
    public Page<User> getConsultantAccounts(Pageable pageable, Integer id, String name, String username, String universityName, Integer universityId, List<AccountStatus> status, Integer createBy, Integer updateBy){
        List<String> statusesString = (status == null || status.isEmpty()) ? null : status.stream().map( (s) -> s.name()).toList();
        if (statusesString ==null)
            return userRepository.getConsultantAccount(pageable, id, name, username, universityName, universityId, createBy, updateBy);
        else
            return userRepository.getConsultantAccount(pageable, id, name, username, universityName, universityId, statusesString, createBy, updateBy);
    }
}
