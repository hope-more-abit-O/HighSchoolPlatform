package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.RegisterAdminRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.AdminInfo;
import com.demo.admissionportal.repository.AdminInfoRepository;
import com.demo.admissionportal.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminInfoRepository adminInfoRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseData<AdminInfo> registerAdmin(RegisterAdminRequestDTO request) {
        if (adminInfoRepository.findByUsername(request.getUsername()).isPresent()) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Username đã tồn tại !");
        }
        if (adminInfoRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Email đã tồn tại !");
        }
        AdminInfo adminInfo = modelMapper.map(request, AdminInfo.class);
        adminInfo.setPassword(passwordEncoder.encode(request.getPassword()));
        adminInfo.setRole(Role.ADMIN);
        adminInfo.setAvatar("image.png");
        adminInfo.setCreateTime(new Date());
        adminInfo.setStatus(AccountStatus.ACTIVE);

        adminInfoRepository.save(adminInfo);

        return new ResponseData<>(ResponseCode.C200.getCode(), "Quản trị viên được tạo thành công !", adminInfo);
    }
}