package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private final StudentRepository studentRepository;
    private final ConsultantRepository consultantRepository;
    private final UniversityRepository universityRepository;
    private final AdminRepository adminRepository;
    private final StaffRepository staffRepository;

    public boolean validateUsernameAndEmailAvailable(String username, String email) throws Exception {
        log.info("Checking email and username availability.");
        String errorMessage = "Tên tài khoản hoặc email đã được sử dụng";
        if ((adminRepository.findFirstByUsernameOrEmail(username, email).isPresent()) ||
                (staffRepository.findFirstByUsernameOrEmail(username, email).isPresent()) ||
                (universityRepository.findFirstByUsernameOrEmail(username,email).isPresent()) ||
                (consultantRepository.findByEmailOrUsername(email, username).isPresent()) ||
                (studentRepository.findFirstByUsernameOrEmail(username, email).isPresent())
        ){
            log.warn("Username: {} or email: {} not available!", username, email);
            throw new Exception(errorMessage);
        }
        return false;
    }
}
