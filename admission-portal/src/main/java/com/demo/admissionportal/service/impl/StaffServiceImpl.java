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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ValidationService validationService;

    @Override
    @Transactional
    public ResponseData registerStaff(RegisterStaffRequestDTO request) {
        try {
            log.info("Trimming request data");
            request.trim();
            log.info("Check if username, email, phone are available or not.");
            validationService.validateRegister(request.getUsername(), request.getEmail(), request.getPhone());
            String password = RandomStringUtils.randomAlphanumeric(9);
            Integer adminId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            log.info("Get current admin account id: {}", adminId);
            log.info("Storing staff's account.");
            User staff = userRepository.save(new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(password), Role.STAFF, adminId));
            if (staff == null) {
                log.error("Store staff's account failed!");
                throw new StoreDataFailedException("Lưu tài khoản nhân viên thất bại.");
            }
            log.info("Storing staff's information.");
            StaffInfo staffInfostaff =new StaffInfo(
                    staff.getId(),
                    adminId,
                    request.getFirstName(),
                    request.getMiddleName(),
                    request.getLastName(),
                    request.getPhone()
            );
            StaffInfo staffInfo = staffInfoRepository.save(staffInfostaff);
            if (staffInfo == null) {
                log.error("Storing staff's information failed.");
                throw new StoreDataFailedException("Lưu thông tin nhân viên thất bại.");
            }
            log.info("Storing staff's information succeed.");

            emailUtil.sendAccountPasswordRegister(staff, password);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Nhân viên được tạo thành công !");
        } catch (DataExistedException de) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Username hoặc email, số điện thoại đã tồn tại");
        } catch (Exception ex) {
            log.error("Error occurred while registering: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi tạo tài khoản");
        }
    }


    @Override
    public ResponseData<Page<StaffResponseDTO>> findAll(String username, String firstName, String middleName, String lastName, String email, String phone, AccountStatus status, Pageable pageable) {
        log.info("Get all staff with filters: Username: {}, firstName: {}, middleName: {}, lastName: {}, Email: {}, Phone: {}, Status: {}", username, firstName, middleName, lastName, email, phone, status);
        List<StaffResponseDTO> staffResponse = new ArrayList<>();
        String statusString = status != null ? status.name() : null;
        Page<StaffInfo> staffPage = staffInfoRepository.findAll(username, firstName, middleName, lastName, email, phone, statusString, pageable);
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
//        Optional<StaffInfo> existStaffOpt = staffInfoRepository.findById(id);
//
//        if (existStaffOpt.isEmpty()) {
//            log.warn("Staff with id: {} not found", id);
//            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhân viên với mã: " + id);
//        }
//
//        StaffInfo existStaff = existStaffOpt.get();
//        if (!existStaff.getEmail().equals(request.getEmail())) {
//            Optional<User> existUser = userRepository.findByEmail(request.getEmail());
//            if (existUser.isPresent() && !existUser.get().getId().equals(existStaff.getId())) {
//                return new ResponseData<>(ResponseCode.C204.getCode(), "Email đã tồn tại !");
//            }
//        }
//        try {
//            log.info("Starting update process for Staff name: {} {} {}", request.getFirstName(), request.getMiddleName(), request.getLastName());
//
//            existStaff.setFirstName(request.getFirstName());
//            existStaff.setMiddleName(request.getMiddleName());
//            existStaff.setLastName(request.getLastName());
//            existStaff.setPhone(request.getPhone());
//
//            staffInfoRepository.save(existStaff);
//            StaffResponseDTO staffResponseDTO = modelMapper.map(existStaff, StaffResponseDTO.class);
//            log.info("Staff updated successfully with ID: {}", existStaff.getId());
//            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật thành công!", staffResponseDTO);
//        } catch (Exception e) {
//            log.error("Error updating staff with id: {}", id, e);
//            return new ResponseData<>(ResponseCode.C201.getCode(), "Cập nhật thất bại, vui lòng thử lại sau!");
//        }
        return null;
    }

    @Override
    public ResponseData<?> deleteStaffById(int id, DeleteStaffRequest request) {
//        try {
//            log.info("Starting delete process for staff ID: {}", id);
//            User existingStaff = userRepository.findById(id).orElse(null);
//            if (existingStaff == null || existingStaff.getStatus().equals(AccountStatus.INACTIVE)) {
//                log.warn("Staff with ID: {} not found", id);
//                return new ResponseData<>(ResponseCode.C203.getCode(), "Nhân viên không tồn tại !");
//            }
//            existingStaff.setStatus(AccountStatus.INACTIVE);
//            existingStaff.setNote(request.note());
//            staffInfoRepository.save(existingStaff);
//            log.info("Staff with ID: {} is INACTIVE", id);
//            return new ResponseData<>(ResponseCode.C200.getCode(), "Xóa nhân viên thành công !");
//        } catch (Exception e) {
//            log.error("Delete staff with ID failed: {}", e.getMessage());
//            return new ResponseData<>(ResponseCode.C201.getCode(), "Xóa nhân viên thất bại, vui lòng kiểm tra lại !");
//        }
        return null;
    }

    @Override
    public ResponseData<?> activateStaffById(int id, ActiveStaffRequest request) {
//        try {
//            StaffInfo existingStaff = staffInfoRepository.findById(id).orElse(null);
//            if (existingStaff == null) {
//                log.warn("Staff with ID: {} not found", id);
//                return new ResponseData<>(ResponseCode.C203.getCode(), "Nhân viên không tồn tại!");
//            }
//            if (existingStaff.getStatus() == AccountStatus.ACTIVE.name()){
//                return new ResponseData<>(ResponseCode.C201.getCode(), "Nhân viên đang hoạt động !");
//            }
//             AccountStatus.INACTIVE.name().equals(existingStaff.getStatus());
//                log.info("Activating INACTIVE staff with ID: {}", id);
//                existingStaff.setStatus(AccountStatus.ACTIVE.name());
//                existingStaff.setNote(request.note());
//                staffInfoRepository.save(existingStaff);
//                return new ResponseData<>(ResponseCode.C200.getCode(), "Kích hoạt nhân viên thành công!");
//
//        } catch (Exception e) {
//            log.error("Activation of staff with ID: {} failed: {}", id, e.getMessage());
//            return new ResponseData<>(ResponseCode.C201.getCode(), "Kích hoạt nhân viên thất bại, vui lòng kiểm tra lại!");
//        }
//    }

        return null;
    }
}