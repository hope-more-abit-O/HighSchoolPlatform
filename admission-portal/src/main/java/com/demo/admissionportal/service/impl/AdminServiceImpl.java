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
            log.info("Starting registration process for email: {}, username: {}", request.getEmail(), request.getUsername());
            if (request == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Request is empty");
            }
            // Case 1 : Existed By Email
            String email = request.getEmail().trim();
            Student checkStudentExistedByEmail = studentRepository.findByEmail(email);
            Staff checkStaffExistedByEmail = staffRepository.findByEmail(email);
            Optional<Consultant> checkConsultantExistedByEmail = consultantRepository.findByEmail(email);
            University checkUniversityExistedByEmail = universityRepository.findByEmail(email);
            Admin checkAdminExistedByEmail = adminRepository.findByEmail(email);
            if (checkStudentExistedByEmail != null || checkStaffExistedByEmail != null || checkConsultantExistedByEmail.isPresent()
                    || checkUniversityExistedByEmail != null || checkAdminExistedByEmail != null) {
                log.error("Email {} is already existed", email);
                return new ResponseData<>(ResponseCode.C204.getCode(), "Email đã được đăng kí bởi một tài khoản khác !");
            }
            // Case 2: Existed By UserName
            String username = request.getUsername().trim();
            Optional<Student> checkStudentExistedByUserName = studentRepository.findByUsername(username);
            Optional<Staff> checkStaffExistedByUserName = staffRepository.findByUsername(username);
            Optional<Consultant> checkConsultantExistedByUserName = consultantRepository.findByUsername(username);
            Optional<University> checkUniversityExistedByUserName = universityRepository.findByUsername(username);
            Optional<Admin> checkAdminExistedByUsername = adminRepository.findByUsername(username);
            if (checkStudentExistedByUserName.isPresent() || checkStaffExistedByUserName.isPresent() || checkConsultantExistedByUserName.isPresent()
                    || checkUniversityExistedByUserName.isPresent() || checkAdminExistedByUsername.isPresent()) {
                log.error("Username {} is already existed", username);
                return new ResponseData<>(ResponseCode.C204.getCode(), "Tên đăng nhập đã được đăng kí bởi một tài khoản khác !");
            }
            // Case 3: Existed By Phone
            String phone = request.getPhone().trim();
            Student checkStudentExistedByPhone = studentRepository.findByPhone(phone);
            Staff checkStaffExistedExistedByPhone = staffRepository.findByPhone(phone);
            Optional<Consultant> checkConsultantExistedByPhone = consultantRepository.findByPhone(phone);
            Optional<University> checkUniversityExistedByPhone = universityRepository.findByPhone(phone);
            Admin checkAdminExistedByPhone = adminRepository.findAdminByPhone(phone);
            if (checkStudentExistedByPhone != null || checkStaffExistedExistedByPhone != null || checkConsultantExistedByPhone.isPresent()
                    || checkUniversityExistedByPhone.isPresent() || checkAdminExistedByPhone != null) {
                log.error("Phone {} is already existed", phone);
                return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoại đã được đăng kí sử dụng bởi một tài khoản khác !");
            }
            Admin newAdmin = modelMapper.map(request, Admin.class);
            newAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
            newAdmin.setRole(Role.ADMIN);
            newAdmin.setStatus(AccountStatus.ACTIVE.name());
            adminRepository.save(newAdmin);
            log.info("Admin registered successfully with email: {}, username: {}", email, username);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Quản trị viên được tạo thành công!", newAdmin);
        } catch (Exception e) {
            log.error("Registering admin with email: {}, username: {} failed: {}", request.getEmail(), request.getUsername(), e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Quản trị viên tạo thất bại!", e);
        }
    }
}
