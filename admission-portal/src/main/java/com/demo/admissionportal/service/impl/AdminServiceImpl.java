package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
//    private final AdminInfoRepository adminInfoRepository;
//    private final ModelMapper modelMapper;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public ResponseData<AdminInfo> registerAdmin(RegisterAdminRequestDTO request) {
//        if (adminInfoRepository.findByUsername(request.getUsername()).isPresent()) {
//            return new ResponseData<>(ResponseCode.C204.getCode(), "Username đã tồn tại !");
//        }
//        if (adminInfoRepository.findByEmail(request.getEmail()).isPresent()) {
//            return new ResponseData<>(ResponseCode.C204.getCode(), "Email đã tồn tại !");
//        }
//        AdminInfo adminInfo = modelMapper.map(request, AdminInfo.class);
//        adminInfo.setPassword(passwordEncoder.encode(request.getPassword()));
//        adminInfo.setRole(Role.ADMIN);
//        adminInfo.setAvatar("image.png");
//        adminInfo.setCreateTime(new Date());
//        adminInfo.setStatus(AccountStatus.ACTIVE);
//
//        adminInfoRepository.save(adminInfo);
//
//        return new ResponseData<>(ResponseCode.C200.getCode(), "Quản trị viên được tạo thành công !", adminInfo);
//    }
}