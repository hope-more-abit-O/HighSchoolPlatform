package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.ActiveStaffRequest;
import com.demo.admissionportal.dto.request.DeleteStaffRequest;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.StaffResponseDTO;
import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.repository.StaffInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.StaffService;
import com.demo.admissionportal.service.ValidationService;
import com.demo.admissionportal.util.impl.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final ValidationService validationService;

    @Override
    @Transactional
    public ResponseData registerStaff(RegisterStaffRequestDTO request) {
        try {
            log.info("Trimming request data");
            request.trim();

            log.info("Generate username for the new staff.");
            String username = generateUsername();

            log.info("Check if email or phone are available or not.");
            validationService.validateRegister(null, request.getEmail(), request.getPhone());

            String password = RandomStringUtils.randomAlphanumeric(9);
            Integer adminId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            log.info("Get current admin account id: {}", adminId);

            log.info("Storing staff's account.");
            User staff = userRepository.save(new User(username, request.getEmail(), passwordEncoder.encode(password), Role.STAFF, adminId));
            if (staff == null) {
                log.error("Store staff's account failed!");
                throw new StoreDataFailedException("Lưu tài khoản nhân viên thất bại.");
            }

            log.info("Storing staff's information.");
            StaffInfo staffInfo = new StaffInfo(staff.getId(), adminId, request.getFirstName(), request.getMiddleName(), request.getLastName(), request.getPhone());
            StaffInfo savedStaffInfo = staffInfoRepository.save(staffInfo);
            if (savedStaffInfo == null) {
                log.error("Storing staff's information failed.");
                throw new StoreDataFailedException("Lưu thông tin nhân viên thất bại.");
            }

            log.info("Storing staff's information succeed.");
            emailUtil.sendAccountPasswordRegister(staff, password);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Nhân viên được tạo thành công !");
        } catch (DataExistedException de) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Email hoặc số điện thoại đã tồn tại");
        } catch (Exception ex) {
            log.error("Error occurred while registering: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi tạo tài khoản");
        }
    }

    private String generateUsername() {
        String lastStaffUsername = userRepository.findLastStaffUsername();
        int nextStaffNumber = 1;
        if (lastStaffUsername != null) {
            String numberPart = lastStaffUsername.substring(5);
            nextStaffNumber = Integer.parseInt(numberPart) + 1;
        }
        return "staff" + nextStaffNumber;
    }


    @Override
    public ResponseData<Page<StaffResponseDTO>> findAll(String username, String firstName, String middleName, String lastName, String email, String phone, AccountStatus status, Pageable pageable) {
        log.info("Get all staff with filters: Username: {}, firstName: {}, middleName: {}, lastName: {}, Email: {}, Phone: {}, Status: {}", username, firstName, middleName, lastName, email, phone, status);
        List<StaffResponseDTO> staffResponse = new ArrayList<>();
        String statusString = status != null ? status.name() : null;
        Page<StaffInfo> staffPage = staffInfoRepository.findAll(username, firstName, middleName, lastName, email, phone, statusString, pageable);
        staffPage.forEach(staffInfo -> {
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setId(staffInfo.getId());
            staffResponseDTO.setUsername(staffInfo.getUser().getUsername());
            staffResponseDTO.setName(staffInfo.getFirstName() + " " + staffInfo.getMiddleName() + " " + staffInfo.getLastName());
            staffResponseDTO.setPhone(staffInfo.getPhone());
            staffResponseDTO.setStatus(staffInfo.getUser().getStatus().name());
            staffResponseDTO.setEmail(staffInfo.getUser().getEmail());
            staffResponseDTO.setAvatar(staffInfo.getUser().getAvatar());
            staffResponseDTO.setNote(staffInfo.getUser().getNote());
            staffResponse.add(staffResponseDTO);
        });
        Page<StaffResponseDTO> result = new PageImpl<>(staffResponse, staffPage.getPageable(), staffPage.getTotalElements());
        log.info("Successfully get list of staffs: {}", staffPage);
        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), result);
    }

    @Override
    public ResponseData<?> getStaffById(int id) {

        Optional<StaffInfo> staffOpt = staffInfoRepository.findById(id);
        if (staffOpt.isEmpty()) {
            log.warn("Staff with id: {} not found", id);
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhân viên này !");
        }
        StaffInfo existStaff = staffOpt.get();
        User userStaff = userRepository.findById(existStaff.getId()).orElseThrow(() -> new IllegalStateException("Không tìm thấy nhân viên !"));
        StaffResponseDTO staffResponseDTO = modelMapper.map(existStaff, StaffResponseDTO.class);
        staffResponseDTO.setUsername(userStaff.getUsername());
        staffResponseDTO.setEmail(userStaff.getEmail());
        staffResponseDTO.setName(existStaff.getFirstName() + " " + existStaff.getMiddleName() + " " + existStaff.getLastName());
        staffResponseDTO.setAvatar(userStaff.getAvatar());
        staffResponseDTO.setStatus(userStaff.getStatus().name());

        log.info("Staff details retrieved successfully for ID: {}", id);
        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), staffResponseDTO);
    }


    @Override
    public ResponseData<StaffResponseDTO> updateStaff(UpdateStaffRequestDTO request, Integer id, String token) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || !userOpt.get().getUsername().equals(token)) {
            log.warn("Unauthorized staff with token: {}", token);
            return new ResponseData<>(ResponseCode.C209.getCode(), "Không thể xác thực nhân viên này !");
        }

        Optional<StaffInfo> existStaffOpt = staffInfoRepository.findById(id);
        Optional<StaffInfo> existStaffByPhone = staffInfoRepository.findFirstByPhone(request.getPhone());
        if (existStaffByPhone.isPresent() && !existStaffByPhone.get().getId().equals(id)) {
            log.info("Phone of staff {}: ", request.getPhone());
            return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoại đã tồn tại: " + request.getPhone());
        }

        if (existStaffOpt.isEmpty()) {
            log.warn("Staff with id: {} not found", id);
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhân viên với mã: " + id);
        }
        StaffInfo existStaff = existStaffOpt.get();
        try {
            log.info("Starting update process for Staff name: {} {} {}", request.getFirstName(), request.getMiddleName(), request.getLastName());
            existStaff.setFirstName(request.getFirstName());
            existStaff.setMiddleName(request.getMiddleName());
            existStaff.setLastName(request.getLastName());
            existStaff.setPhone(request.getPhone());

            User staff = userRepository.findById(existStaff.getId()).orElseThrow(() -> new IllegalStateException("Không tìm thấy nhân viên !"));
            staff.setAvatar(request.getAvatar());
            userRepository.save(staff);
            staffInfoRepository.save(existStaff);
            StaffResponseDTO staffResponseDTO = modelMapper.map(existStaff, StaffResponseDTO.class);
            staffResponseDTO.setUsername(staff.getUsername());
            staffResponseDTO.setName(existStaff.getFirstName() + " " + existStaff.getMiddleName() + " " + existStaff.getLastName());
            staffResponseDTO.setEmail(staff.getEmail());
            staffResponseDTO.setAvatar(staff.getAvatar());
            staffResponseDTO.setStatus(staff.getStatus().name());
            log.info("Staff updated successfully with ID: {}", existStaff.getId());
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
            User existingStaff = userRepository.findById(id).orElse(null);
            if (existingStaff == null || existingStaff.getStatus().equals(AccountStatus.INACTIVE)) {
                log.warn("Staff with ID: {} not found", id);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Nhân viên không tồn tại !");
            }
            existingStaff.setStatus(AccountStatus.INACTIVE);
            existingStaff.setNote(request.note());
            userRepository.save(existingStaff);
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
            User existingStaff = userRepository.findById(id).orElse(null);
            if (existingStaff == null) {
                log.warn("Staff with ID: {} not found", id);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Nhân viên không tồn tại!");
            }
            if (AccountStatus.ACTIVE.equals(existingStaff.getStatus())) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Nhân viên đang hoạt động!");
            } else {
                log.info("Activating INACTIVE staff with ID: {}", id);
                existingStaff.setStatus(AccountStatus.ACTIVE);
                existingStaff.setNote(request.note());
                userRepository.save(existingStaff);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Kích hoạt nhân viên thành công!");
            }
        } catch (Exception e) {
            log.error("Activation of staff with ID: {} failed: {}", id, e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Kích hoạt nhân viên thất bại, vui lòng kiểm tra lại!");
        }
    }
}