package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.user.FullUserResponseDTO;
import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import com.demo.admissionportal.dto.request.ChangeStatusUserRequestDTO;
import com.demo.admissionportal.dto.request.UpdateUserRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.UpdateUserResponseDTO;
import com.demo.admissionportal.dto.response.UserProfileResponseDTO;
import com.demo.admissionportal.dto.response.UserResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.exception.NotAllowedException;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.UserService;
import com.demo.admissionportal.service.ValidationService;
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
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final ModelMapper modelMapper;
    private final ValidationService validationService;



    @Override
    public ResponseData<Page<UserResponseDTO>> getUser(String username, String email, Pageable pageable) {
        try {
            List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
            Page<UserInfo> userPage = userInfoRepository.findAll(username, email, pageable);
            // Map UserInfo to UserResponseDTO
            userPage.forEach(userInfo -> {
                UserResponseDTO responseDTO = new UserResponseDTO();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = formatter.format(new Date());
                responseDTO.setUsername(userInfo.getUser().getUsername());
                responseDTO.setEmail(userInfo.getUser().getEmail());
                responseDTO.setName(userInfo.getFirstName() + " " + userInfo.getMiddleName() + " " + userInfo.getLastName());
                responseDTO.setStatus(userInfo.getUser().getStatus().name());
                responseDTO.setCreate_time(dateString);
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

            if (id == null || id < 0 || requestDTO == null) {
                new ResponseEntity<ResponseData<UpdateUserResponseDTO>>(HttpStatus.BAD_REQUEST);
            }
            UserInfo userInfo = userInfoRepository.findUserInfoById(id);
            User user = userRepository.findUserById(id);
            // Get user profile
            if (userInfo == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");
            }
            // Update profile
            userInfo.setFirstName(requestDTO.getFirstname());
            userInfo.setMiddleName(requestDTO.getMiddle_name());
            userInfo.setLastName(requestDTO.getLastname());
            userInfo.setGender(requestDTO.getGender());
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
        } catch (Exception ex) {
            log.error("Error while update user: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi cập nhật user");
        }
    }

    @Override
    public ResponseData<ChangeStatusUserRequestDTO> changeStatus(Integer id, ChangeStatusUserRequestDTO requestDTO) {
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
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã cập nhật trạng thái thành công");
        } catch (Exception ex) {
            log.error("Error while change status user: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi thay đổi trạng thái user");
        }
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("User's account with id: {} not found.", id);
            return new ResourceNotFoundException("User's account with id: " + id + " not found");
        });
    }

    @Override
    public FullUserResponseDTO mappingResponse(User user) throws ResourceNotFoundException {
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
            userRepository.save(account);
        } catch (Exception e) {
            throw new StoreDataFailedException("Cập nhập trạng thái tư vấn viên thất bại.");
        }
        return account;
    }

    public User updateUser(Integer id, String username, String email, Integer updateById, String name) throws StoreDataFailedException{
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
                        user -> new ActionerDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name())
                ));
    }

    private FullUserResponseDTO mapToFullUserResponseDTO(User user, Map<Integer, ActionerDTO> actionerMap) {
        FullUserResponseDTO fullUserResponseDTO = modelMapper.map(user, FullUserResponseDTO.class);
        fullUserResponseDTO.setCreateBy(actionerMap.get(user.getCreateBy()));
        fullUserResponseDTO.setUpdateBy(actionerMap.get(user.getUpdateBy()));
        return fullUserResponseDTO;
    }
}
