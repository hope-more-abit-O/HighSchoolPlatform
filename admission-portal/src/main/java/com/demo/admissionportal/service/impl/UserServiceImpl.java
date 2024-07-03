package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.UserProfileResponseDTO;
import com.demo.admissionportal.dto.response.UserResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    @Override
    public ResponseData<List<UserResponseDTO>> getUser() {
        try {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            List<User> users = userRepository.findAll();
            List<UserInfo> userInfos = userInfoRepository.findAll();
            List<UserResponseDTO> userResponseDTOs = new ArrayList<>();
            for (User user : users) {
                // Map user with user_info
                UserInfo userInfo = userInfos.stream()
                        .filter(ui -> ui.getId().equals(user.getId()))
                        .findFirst()
                        .orElse(null);
                if (userInfo != null) {
                    // Add element userResponseDTO to List<userResponseDTO>
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = formatter.format(new Date());
                    userResponseDTO.setUsername(user.getUsername());
                    userResponseDTO.setEmail(user.getEmail());
                    userResponseDTO.setName(userInfo.getFirstname() + " " + userInfo.getMiddleName() + " " + userInfo.getLastName());
                    userResponseDTO.setStatus(user.getStatus());
                    userResponseDTO.setCreate_time(dateString);
                    userResponseDTO.setNote(user.getNote());
                    userResponseDTOs.add(userResponseDTO);
                }
            }
            if (userResponseDTOs.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy danh sách user", userResponseDTOs);
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
            Ward ward = wardRepository.findWardById(userInfo.getWard());
            District district = districtRepository.findDistrictById(userInfo.getDistrict());
            Province province = provinceRepository.findProvinceById(userInfo.getProvince());

            UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO();
            userProfileResponseDTO.setEmail(user.getEmail());
            userProfileResponseDTO.setUsername(user.getUsername());
            userProfileResponseDTO.setName(userInfo.getFirstname() + " " + userInfo.getMiddleName() + " " + userInfo.getLastName());
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
    public List<User> getListUser() {
        List<User> ls = userRepository.findByRole(Role.USER);
        return ls;
    }
}
