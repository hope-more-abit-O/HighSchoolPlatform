package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.RegisterAdminRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Admin;
import com.demo.admissionportal.repository.AdminRepository;
import com.demo.admissionportal.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * The type Admin service.
 */
@Service
@Slf4j
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseData<?> registerAdmin(RegisterAdminRequestDTO request) {
        try {
            log.info("Starting registration process for email, username: {}", request.getEmail(), request.getUsername());
            Admin existAdmin = adminRepository.findAdminByUsernameAndEmail(request.getUsername().trim(), request.getEmail().trim());
            if (existAdmin != null) {
                log.warn("Admin with email: {} or username: {} already exists", request.getEmail(), request.getUsername());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Tên đăng nhập hoặc Email đã tồn tại !");
            }
            Admin adminHasPhone = adminRepository.findAdminByPhone(request.getPhone());
            if (adminHasPhone != null) {
                log.warn("Staff with phone: {} already registered", request.getPhone().trim());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoại đã được đăng kí bởi một Quản Trị Viên khác !");
            }
            Admin newAdmin = modelMapper.map(request, Admin.class);
            newAdmin.setStatus(AccountStatus.ACTIVE.name());
            adminRepository.save(newAdmin);
            log.info("Staff registered successfully with email and username: {}", request.getEmail(), request.getUsername());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Quản Trị Viên được tạo thành công !", newAdmin);
        } catch (Exception e) {
            log.error("Registering admin with email and username failed: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Tạo Quản Trị Viên thất bại !", e);
        }

    }
}
