package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.response.RegisterStaffResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.StaffResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public ResponseData<Page<StaffResponseDTO>> findAll(String username, String name, String email, String phone, Pageable pageable) {
        log.info("Get all staff with filters: Username: {}, Name: {}, Email: {}, Phone: {}", username, name, email, phone);
        List<StaffResponseDTO> staffResponse = new ArrayList<>();
        Page<StaffInfo> staffPage = staffInfoRepository.findAll(username, name, email, phone, pageable);
        staffPage.getContent().forEach(s -> staffResponse.add(modelMapper.map(s, StaffResponseDTO.class)));
        Page<StaffResponseDTO> result = new PageImpl<>(staffResponse, staffPage.getPageable(), staffPage.getTotalElements());
        log.info("Successfully retrieved list of staffs:{}", staffPage);
        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), result);
    }


}
