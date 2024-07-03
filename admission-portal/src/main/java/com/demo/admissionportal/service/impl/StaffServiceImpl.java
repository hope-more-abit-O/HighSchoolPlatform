package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.RegisterStaffRequest;
import com.demo.admissionportal.dto.response.PostAccountResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.repository.StaffRepository;
import com.demo.admissionportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl {
    private final StaffRepository staffRepository;
    private final ValidationServiceImpl validationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseData<PostAccountResponse> createStaff(RegisterStaffRequest request) throws DataExistedException {
        validationService.validateRegister(request.getUsername(), request.getEmail(), request.getPhone());

        User staff = new User(request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.STAFF,
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        staff = userRepository.save(staff);
        StaffInfo staffInfo = new StaffInfo(staff.getId(), staff.getCreateBy(), request.getFirstName(), request.getMiddleName(), request.getLastName(), request.getPhone());

        staffRepository.save(staffInfo);
        return ResponseData.created("Tạo tài khoản nhân viên thành công.");
    }
}
