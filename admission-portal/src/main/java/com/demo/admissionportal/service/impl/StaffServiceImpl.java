package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.ActiveStaffRequest;
import com.demo.admissionportal.dto.request.DeleteStaffRequest;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Username đã tồn tại !");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
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
        staffInfo.setStatus(AccountStatus.ACTIVE);
        staffInfo.setAvatar("image.png");
        staffInfo.setCreateTime(new Date());
        staffInfo.setNote(null);
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
    public ResponseData<Page<StaffResponseDTO>> findAll(String username, String firstName,String middleName, String lastName, String email, String phone, String status, Pageable pageable) {
        log.info("Get all staff with filters: Username: {}, firstName: {}, middleName: {}, lastName: {}, Email: {}, Phone: {}, Status: {}", username, firstName, middleName, lastName, email, phone, status);
        List<StaffResponseDTO> staffResponse = new ArrayList<>();
        Page<StaffInfo> staffPage = staffInfoRepository.findAll(username, firstName, middleName, lastName, email, phone, status, pageable);
        staffPage.getContent().forEach(s -> staffResponse.add(modelMapper.map(s, StaffResponseDTO.class)));
        Page<StaffResponseDTO> result = new PageImpl<>(staffResponse, staffPage.getPageable(), staffPage.getTotalElements());
        log.info("Successfully retrieved list of staffs:{}", staffPage);
        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), result);
    }

    @Override
    public ResponseData<?> getStaffById(int id) {
        Optional<StaffInfo> staff = staffInfoRepository.findById(id);
        if (staff.isEmpty()) {
            log.warn("Staff with id: {} not found", id);
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhân viên này !");
        }
        StaffInfo getStaff = staff.get();
        StaffResponseDTO result = modelMapper.map(getStaff, StaffResponseDTO.class);
        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), result);
    }

    @Override
    public ResponseData<StaffResponseDTO> updateStaff(UpdateStaffRequestDTO request, Integer id) {
        Optional<StaffInfo> existStaff = staffInfoRepository.findById(id);

        if (existStaff.isEmpty()) {
            log.warn("Staff with id: {} not found", id);
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhân viên với mã: " + id);
        }
        StaffInfo staff = existStaff.get();
        if (!staff.getEmail().equals(request.getEmail())) {
            Optional<User> existUser = userRepository.findByEmail(request.getEmail());
            if (existUser.isPresent() && !existUser.get().getId().equals(staff.getId())) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Email đã tồn tại !");
            }
        }
        try {
            log.info("Starting update process for Staff name: {} {} {}", request.getFirstName(), request.getMiddleName(), request.getLastName());
            modelMapper.map(request, staff);
            staff.setPassword(passwordEncoder.encode(request.getPassword()));
            staffInfoRepository.save(staff);
            StaffResponseDTO staffResponseDTO = modelMapper.map(staff, StaffResponseDTO.class);
            log.info("Staff updated successfully with ID: {}", staff.getId());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật thành công!", staffResponseDTO);
        } catch (Exception e) {
            log.error("Error updating staff with id: {}", id, e);
            return new ResponseData<>(ResponseCode.C201.getCode(), "Cập nhật thất bại, vui lòng thử lại sau!");
        }
    }

    @Override
    public ResponseData<?> deleteStaffById(int id, DeleteStaffRequest request) {
        try {
            log.info("Starting delete process for staff ID: {}", id);
            StaffInfo existingStaff = staffInfoRepository.findById(id).orElse(null);
            if (existingStaff == null) {
                log.warn("Staff with ID: {} not found", id);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Nhân viên không tồn tại !");
            }
            existingStaff.setStatus(AccountStatus.INACTIVE);
            existingStaff.setNote(request.note());
            staffInfoRepository.save(existingStaff);
            log.info("Staff with ID: {} is INACTIVE", id);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Xóa nhân viên thành công !");
        } catch (Exception e) {
            log.error("Delete staff with ID failed: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Xóa nhân viên thất bại, vui lòng kiểm tra lại !");
        }
    }

    @Override
    public ResponseData<?> activateStaffById(int id, ActiveStaffRequest request) {
        try {
            Optional<StaffInfo> optionalStaff = staffInfoRepository.findById(id);
            if (optionalStaff.isEmpty()) {
                log.warn("Staff with ID: {} not found", id);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Nhân viên không tồn tại!");
            }

            StaffInfo existingStaff = optionalStaff.get();
            if (AccountStatus.INACTIVE.name().equals(existingStaff.getStatus())) {
                log.info("Activating INACTIVE staff with ID: {}", id);
                existingStaff.setStatus(AccountStatus.ACTIVE);
                existingStaff.setNote(request.note());
                staffInfoRepository.save(existingStaff);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Kích hoạt nhân viên thành công!");
            } else {
                log.info("Staff with ID: {} is already ACTIVE", id);
                return new ResponseData<>(ResponseCode.C204.getCode(), "Nhân viên đã được kích hoạt trước đó!");
            }
        } catch (Exception e) {
            log.error("Activation of staff with ID: {} failed: {}", id, e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Kích hoạt nhân viên thất bại, vui lòng kiểm tra lại!");
        }
    }

}
