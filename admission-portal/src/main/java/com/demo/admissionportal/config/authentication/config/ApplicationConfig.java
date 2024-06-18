package com.demo.admissionportal.config.authentication.config;

import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * The type Application config.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final AdminRepository adminRepository;
    private final ConsultantRepository consultantRepository;
    private final UniversityRepository universityRepository;

    /**
     * User details service user details service.
     *
     * @return the user details service
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<Student> studentDetails = studentRepository.findByUsername(username)
                    .or(() -> Optional.ofNullable(studentRepository.findByEmail(username)));
            if (studentDetails.isPresent()) {
                return studentDetails.get();
            }
            Optional<Staff> staffDetails = staffRepository.findByUsername(username)
                    .or(() -> Optional.ofNullable(staffRepository.findByEmail(username)));
            if (staffDetails.isPresent()) {
                return staffDetails.get();
            }
            Optional<Admin> adminDetails = adminRepository.findByUsername(username)
                    .or(() -> Optional.ofNullable(adminRepository.findByEmail(username)));
            if (adminDetails.isPresent()) {
                return adminDetails.get();
            }
            Optional<Consultant> consultantDetails = consultantRepository.findByUsername(username)
                    .or(() -> consultantRepository.findByEmail(username));
            if (consultantDetails.isPresent()) {
                return consultantDetails.get();
            }
            Optional<University> universityDetails = universityRepository.findByUsername(username)
                    .or(() -> Optional.ofNullable(universityRepository.findByEmail(username)));
            if (universityDetails.isPresent()) {
                return universityDetails.get();
            }
            throw new UsernameNotFoundException("Không tìm thấy user");
        };
    }

    /**
     * Authentication provider authentication provider.
     *
     * @return the authentication provider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager authentication manager.
     *
     * @param config the config
     * @return the authentication manager
     * @throws Exception the exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        List<Function<String, UserDetails>> loaders = Arrays.asList(
                this::loadAdminByUsername,
                this::loadStudentByUsername,
                this::loadConsultantByUsername,
                this::loadUniversityByUsername,
                this::loadStaffByUsername
        );
        for (Function<String, UserDetails> loader : loaders) {
            UserDetails userDetails = loader.apply(username);
            if (userDetails != null) {
                return userDetails;
            }
        }
        throw new UsernameNotFoundException("Không tìm thấy user");
    }

    private UserDetails loadStudentByUsername(String username) {
        Optional<Student> studentDetails = studentRepository.findByUsername(username)
                .or(() -> Optional.ofNullable(studentRepository.findByEmail(username)));
        return studentDetails.orElse(null);
    }

    private UserDetails loadAdminByUsername(String username) {
        Optional<Admin> adminDetails = adminRepository.findByUsername(username)
                .or(() -> Optional.ofNullable(adminRepository.findByEmail(username)));
        return adminDetails.orElse(null);
    }

    private UserDetails loadConsultantByUsername(String username) {
        Optional<Consultant> consultantDetails = consultantRepository.findByUsername(username)
                .or(() -> consultantRepository.findByEmail(username));
        return consultantDetails.orElse(null);
    }

    private UserDetails loadUniversityByUsername(String username) {
        Optional<University> universityDetails = universityRepository.findByUsername(username)
                .or(() -> Optional.ofNullable(universityRepository.findByEmail(username)));
        return universityDetails.orElse(null);
    }

    private UserDetails loadStaffByUsername(String username) {
        Optional<Staff> staffDetails = staffRepository.findByUsername(username)
                .or(() -> Optional.ofNullable(staffRepository.findByEmail(username)));
        return staffDetails.orElse(null);
    }
}
