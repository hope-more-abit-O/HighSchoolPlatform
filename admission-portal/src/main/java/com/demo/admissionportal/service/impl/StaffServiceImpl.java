package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Staff;
import com.demo.admissionportal.repository.StaffRepository;
import com.demo.admissionportal.service.StaffService;
import org.springframework.stereotype.Service;

/**
 * The type Staff service.
 */
@Service
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;

    /**
     * Instantiates a new Staff service.
     *
     * @param staffRepository the staff repository
     */
    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public ResponseData<?> registerStaff(RegisterStaffRequestDTO request) {
        try {
            Staff existStaff = staffRepository.findByEmailOrUsername(request.getEmail().trim(), request.getUsername().trim());
            if (existStaff != null) {
                throw new RuntimeException("Username or email already in use");
            }
            Staff newStaff = new Staff();
            newStaff.setName(request.getName());
            newStaff.setUsername(request.getUsername());
            newStaff.setEmail(request.getEmail());
            newStaff.setPassword(request.getPassword());
            newStaff.setAvatar(request.getAvatar());
            newStaff.setPhone(request.getPhone());
            newStaff.setStatus(AccountStatus.ACTIVE.toString());
            staffRepository.save(newStaff);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tao thanh cong", newStaff);
        } catch (Exception e) {
            return new ResponseData<>(ResponseCode.C201.getCode(), "Tao that bai");
        }
    }
}
