package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Staff;
import com.demo.admissionportal.repository.StaffRepository;
import com.demo.admissionportal.service.StaffService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * The type Staff service.
 */
@Service
@Slf4j
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    /**
     * @param request
     * @return
     */
    @Override
    public ResponseData<?> registerStaff(RegisterStaffRequestDTO request) {
        try {
            log.info("Starting registration process for email: {}", request.getEmail());
            Staff existStaff = staffRepository.findByEmailOrUsername(request.getEmail().trim(), request.getUsername().trim());
            if (existStaff != null) {
                log.warn("Staff with email: {} or username: {} already exists", request.getEmail(), request.getUsername());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Tài khoản nhân viên này đã tồn tại !");
            }
            Staff existPhone = staffRepository.findByPhone(request.getPhone().trim());
            if (existPhone != null) {
                log.warn("Staff with phone: {} already registered", request.getPhone().trim());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoại này đã được đăng kí bởi nhân viên khác !");
            }
            Staff newStaff = modelMapper.map(request, Staff.class);
            newStaff.setStatus(AccountStatus.ACTIVE.name());
            newStaff.setRole(Role.STAFF);
            staffRepository.save(newStaff);
            log.info("Staff registered successfully with email: {}", request.getEmail());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Nhân viên được tạo thành công !", newStaff);
        } catch (Exception e) {
            log.error("Registering staff with email failed: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Tạo nhân viên thất bại, vui lòng kiểm tra lại !");
        }
    }
}
