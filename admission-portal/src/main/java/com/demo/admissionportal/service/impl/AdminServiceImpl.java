package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.RegisterAdminRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The type Admin service.
 */
@Service
@Slf4j
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final ConsultantRepository consultantRepository;
    private final UniversityRepository universityRepository;
    private final StudentRepository studentRepository;

    @Override
    public ResponseData<?> registerAdmin(RegisterAdminRequestDTO request) {
        try {
            log.info("Starting registration process for email, username: {}", request.getEmail(), request.getUsername());
            if (request == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Request bị trống");
            }
            // Case 1 : Existed By Email
            Student checkStudentExistedByEmail = studentRepository.findByEmail(request.getEmail().trim());
            Staff checkStaffExistedByEmail = staffRepository.findByEmail(request.getEmail().trim());
            Optional<Consultant> checkConsultantExistedByEmail = consultantRepository.findByEmail(request.getEmail().trim());
            University checkUniversityExistedByEmail = universityRepository.findByEmail(request.getEmail().trim());
            Admin checkAdminExistedByEmail = adminRepository.findByEmail(request.getEmail().trim());
            if (checkStudentExistedByEmail != null || checkStaffExistedByEmail != null || checkConsultantExistedByEmail.isPresent()
                    || checkUniversityExistedByEmail != null || checkAdminExistedByEmail != null) {
                log.error("Email {} is already existed", request.getEmail());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Email đã được tài khoản khác sử dụng");
            }
            // Case 2: Existed By UserName
            Optional<Student> checkStudentExistedByUserName = studentRepository.findByUsername(request.getUsername().trim());
            Optional<Staff> checkStaffExistedByUserName = staffRepository.findByUsername(request.getUsername().trim());
            Optional<Consultant> checkConsultantExistedByUserName = consultantRepository.findByUsername(request.getUsername().trim());
            University checkUniversityExistedByUserName = universityRepository.findByUsername(request.getUsername().trim());
            Optional<Admin> checkAdminExistedByUsername = adminRepository.findByUsername(request.getUsername().trim());
            if (checkStudentExistedByUserName.isPresent() || checkStaffExistedByUserName.isPresent() || checkConsultantExistedByUserName.isPresent()
                    || checkUniversityExistedByUserName != null || checkAdminExistedByUsername.isPresent()) {
                log.error("Username {} is already existed", request.getUsername());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Username đã được tài khoản khác sử dụng");
            }
            // Case 3: Existed By Phone
            Student checkStudentExistedByPhone = studentRepository.findByPhone(request.getPhone().trim());
            Staff checkStaffExistedExistedByPhone = staffRepository.findByPhone(request.getPhone().trim());
            Optional<Consultant> checkConsultantExistedByPhone = consultantRepository.findByPhone(request.getPhone().trim());
            University checkUniversityExistedByPhone = universityRepository.findByPhone(request.getPhone().trim());
            Admin checkAdminExistedByPhone = adminRepository.findAdminByPhone(request.getPhone().trim());
            if (checkStudentExistedByPhone != null || checkStaffExistedExistedByPhone != null || checkConsultantExistedByPhone.isPresent()
                    || checkUniversityExistedByPhone != null || checkAdminExistedByPhone != null) {
                log.error("Phone {} is already existed", request.getPhone());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoại đã được tài khoản khác sử dụng");
            }
            Admin newAdmin = modelMapper.map(request, Admin.class);
            newAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
            newAdmin.setRole(Role.ADMIN);
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
