package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.PaginationDTO;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.StaffService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    private final Map<String, String> allowedSortField = Map.of(
            "name", "name",
            "phone", "phone"
    );
    private Sort getSort(List<String> orders) {
        Sort sort = Sort.unsorted();
        for (String order : orders) {
            String[] parts = order.split("_");
            String field = parts[0];
            String direction = parts[1];
            if (allowedSortField.containsKey(field)) {
                Sort.Order sortOrder = new Sort.Order(Sort.Direction.fromString(direction), allowedSortField.get(field));
                sort = sort.and(Sort.by(sortOrder));
            } else {
                throw new RuntimeException("Invalid sort parameter: " + field);
            }
        }
        return sort;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public ResponseData<?> registerStaff(RegisterStaffRequestDTO request) {
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
            Staff newStaff = modelMapper.map(request, Staff.class);
            newStaff.setPassword(passwordEncoder.encode(request.getPassword()));
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
    @Override
    public PaginationDTO<Staff> getAllStaffs(int page, int size, Map<String, String> filters, List<String> order) {
        Pageable pageable = PageRequest.of(page - 1, size, getSort(order));
        String nameFilter = filters.getOrDefault("name", null);
        String phoneFilter = filters.getOrDefault("phone", null);
        Page<Staff> staffPage = staffRepository.findAll(nameFilter, phoneFilter, pageable);
        return PaginationDTO.<Staff>builder()
                .pageNum(page)
                .pageSize(size)
                .totalPageNum(staffPage.getTotalPages())
                .totalItems(staffPage.getTotalElements())
                .items(staffPage.getContent())
                .build();
    }
    @Override
    public ResponseData<Staff> getStaffById(Integer id) {
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isPresent()) {
            return new ResponseData<>(ResponseCode.C200.getCode(), "Success", staff.get());
        } else {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Staff not found");
        }
    }
    @Override
    public ResponseData<Staff> updateStaff(Integer id, UpdateStaffRequestDTO request) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isPresent()) {
            Staff staff = optionalStaff.get();
            modelMapper.map(request, staff);
            staffRepository.save(staff);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Staff updated successfully", staff);
        } else {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Staff not found");
        }
    }
    @Override
    public ResponseData<Void> deleteStaff(Integer id) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isPresent()) {
            Staff staff = optionalStaff.get();
            staff.setStatus(AccountStatus.INACTIVE.name());
            staffRepository.save(staff);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Staff deleted successfully");
        } else {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Staff not found");
        }
    }
    @Override
    public Staff getStaffById(int id){
        return staffRepository.findById(id).orElse(null);
    }
}
