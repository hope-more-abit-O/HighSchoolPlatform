package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.response.RegisterStaffResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.StaffInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.StaffService;
import com.demo.admissionportal.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final UserRepository userRepository;
    private final StaffInfoRepository staffInfoRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;

    @Override
    public ResponseData<RegisterStaffResponse> registerStaff(RegisterStaffRequestDTO request) {
        if (staffInfoRepository.findByUsername(request.getUsername()).isPresent()) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Username đã tồn tại !");
        }
        if (staffInfoRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Email đã tồn tại !");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        log.debug("Principal type: {}", principal.getClass().getName());
        log.info("Principal type: {}", principal.getClass().getName());
        if (!(principal instanceof User)) {
            return new ResponseData<>(ResponseCode.C205.getCode(), "Người tham chiếu không hợp lệ !");
        }
        User admin = (User) principal;
        Integer adminId = admin.getId();

        String generatedPassword = RandomStringUtils.randomAlphanumeric(10);
        StaffInfo staffInfo = modelMapper.map(request, StaffInfo.class);
        staffInfo.setPassword(passwordEncoder.encode(generatedPassword));
        staffInfo.setRole(Role.STAFF);
        staffInfo.setStatus("ACTIVE");
        staffInfo.setAvatar("image.png");
        staffInfo.setCreateTime(new Date());
        staffInfo.setAdminId(adminId);
        staffInfo.setAdmin(admin);
        staffInfo.setCreateBy(adminId);

        log.debug("StaffInfo createBy before save: {}", staffInfo.getCreateBy());

        staffInfoRepository.save(staffInfo);

        emailUtil.sendStaffPasswordRegister(staffInfo, generatedPassword);

        RegisterStaffResponse response = new RegisterStaffResponse(staffInfo.getUsername(), staffInfo.getEmail(), staffInfo.getFirstName(), staffInfo.getMiddleName(), staffInfo.getLastName(), staffInfo.getPhone());
        return new ResponseData<>(ResponseCode.C200.getCode(), "Nhân viên được tạo thành công !", response);
    }
}
