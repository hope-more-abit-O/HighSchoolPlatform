package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.UserResponseDTO;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserInfo;
import com.demo.admissionportal.repository.UserInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserInfoRepository userInfoRepository;

    @Override
    public ResponseData<UserResponseDTO> getUser(Integer id) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        try {
            if (id == null || id <= 0) {
                return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Sai request");
            }
            User user = userRepository.findUserById(id);
            UserInfo userInfo = userInfoRepository.findUserById(id);
            if (user != null) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = formatter.format(date);
                userResponseDTO.setUsername(user.getUsername());
                userResponseDTO.setEmail(user.getEmail());
                userResponseDTO.setName(userInfo.getFirstname() + " " + userInfo.getMiddleName() + " " + userInfo.getLastName());
                userResponseDTO.setStatus(user.getStatus());
                userResponseDTO.setCreate_time(dateString);
                userResponseDTO.setNote(user.getNote());
                return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy user", userResponseDTO);
            }
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");

        } catch (Exception ex) {
            log.error("Error while get user: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi tìm user");
    }
}
