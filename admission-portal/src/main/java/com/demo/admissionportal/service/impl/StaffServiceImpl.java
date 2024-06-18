package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.StaffResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.StaffService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The type Staff service.
 */
@Service
@Slf4j
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final ConsultantRepository consultantRepository;
    private final UniversityRepository universityRepository;
    private final StudentRepository studentRepository;

    /**
     * @param request
     * @return
     */
    @Override
    public ResponseData<?> registerStaff(RegisterStaffRequestDTO request) {
        try {
            log.info("Starting registration process for email: {}, username: {}", request.getEmail(), request.getUsername());
            if (request == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Yêu cầu tạo tài khoản bị trống !");
            }
            // Case 1 : Existed By Email
            String email = request.getEmail().trim();
            Student checkStudentExistedByEmail = studentRepository.findByEmail(email);
            Staff checkStaffExistedByEmail = staffRepository.findByEmail(email);
            Optional<Consultant> checkConsultantExistedByEmail = consultantRepository.findByEmail(email);
            University checkUniversityExistedByEmail = universityRepository.findByEmail(email);
            Admin checkAdminExistedByEmail = adminRepository.findByEmail(email);
            if (checkStudentExistedByEmail != null || checkStaffExistedByEmail != null || checkConsultantExistedByEmail.isPresent() || checkUniversityExistedByEmail != null || checkAdminExistedByEmail != null) {
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
            if (checkStudentExistedByUserName.isPresent() || checkStaffExistedByUserName.isPresent() || checkConsultantExistedByUserName.isPresent() || checkUniversityExistedByUserName.isPresent() || checkAdminExistedByUsername.isPresent()) {
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
            if (checkStudentExistedByPhone != null || checkStaffExistedExistedByPhone != null || checkConsultantExistedByPhone.isPresent() || checkUniversityExistedByPhone.isPresent() || checkAdminExistedByPhone != null) {
                log.error("Phone {} is already existed", phone);
                return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoại đã được đăng kí sử dụng bởi một tài khoản khác !");
            }
            Staff newStaff = modelMapper.map(request, Staff.class);
            newStaff.setPassword(passwordEncoder.encode(request.getPassword()));
            newStaff.setRole(Role.STAFF);
            newStaff.setStatus(AccountStatus.ACTIVE.name());
            staffRepository.save(newStaff);
            log.info("Admin registered successfully with email: {}, username: {}", email, username);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Nhân viên được tạo thành công!", newStaff);
        } catch (Exception e) {
            log.error("Registering admin with email: {}, username: {} failed: {}", request.getEmail(), request.getUsername(), e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Nhân viên viên tạo thất bại!", e);
        }
    }

    @Override
    public ResponseData<Page<StaffResponseDTO>> findAll(String username, String name, String email, String phone, Pageable pageable) {
        log.info("Get all staff with filters: Username: {}, Name: {}, Email: {}, Phone: {}", username, name, email, phone);
        List<StaffResponseDTO> staffResponse = new ArrayList<>();
        Page<Staff> staffPage = staffRepository.findAll(username, name, email, phone, pageable);
        staffPage.getContent().forEach(s -> staffResponse.add(modelMapper.map(s, StaffResponseDTO.class)));
        Page<StaffResponseDTO> result = new PageImpl<>(staffResponse, staffPage.getPageable(), staffPage.getTotalElements());
        log.info("Successfully retrieved list of staffs", staffPage);
        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), result);
    }

    @Override
    public ResponseData<?> getStaffById(int id) {
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isEmpty()) {
            log.warn("Staff with id: {} not found", id);
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhân viên này !");
        }
        Staff getStaff = staff.get();
        StaffResponseDTO result = modelMapper.map(getStaff, StaffResponseDTO.class);
        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), result);
    }

    @Override
    public ResponseData<StaffResponseDTO> updateStaff(UpdateStaffRequestDTO request, Integer id) {
        Optional<Staff> existStaffOptional = staffRepository.findById(id);

        if (existStaffOptional.isEmpty()) {
            log.warn("Staff with id: {} not found", id);
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy nhân viên với mã {}: " + id);
        }
        Staff existStaff = existStaffOptional.get();
        try {
            log.info("Starting update process for Staff name: {}", request.getName());
            modelMapper.map(request, existStaff);
            existStaff.setPassword(passwordEncoder.encode(request.getPassword()));
            staffRepository.save(existStaff);
            StaffResponseDTO staffResponseDTO = modelMapper.map(existStaff, StaffResponseDTO.class);
            log.info("Staff update successfully with ID: {}", existStaff.getId());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật thành công !", staffResponseDTO);
        } catch (Exception e) {
            log.error("Error updateStaff with id: {}", id, e);
            return new ResponseData<>(ResponseCode.C201.getCode(), "Cập nhật thất bại, vui lòng thử lại sau !");
        }
    }


    @Override
    public ResponseData<?> deleteStaffById(int id) {
        try {
            log.info("Starting delete process for staff ID: {}", id);
            Staff existingStaff = staffRepository.findById(id).orElse(null);
            if (existingStaff == null) {
                log.warn("Staff with ID: {} not found", id);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Nhân viên không tồn tại !");
            }
            existingStaff.setStatus(AccountStatus.INACTIVE.name());
            staffRepository.save(existingStaff);
            log.info("Staff with ID: {} is INACTIVE", id);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Xóa nhân viên thành công !");
        } catch (Exception e) {
            log.error("Delete staff with ID failed: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Xóa nhân viên thất bại, vui lòng kiểm tra lại !");
        }
    }
}