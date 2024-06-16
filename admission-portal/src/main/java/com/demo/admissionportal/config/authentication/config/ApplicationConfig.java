package com.demo.admissionportal.config.authentication.config;

import com.demo.admissionportal.entity.Staff;
import com.demo.admissionportal.entity.Student;
import com.demo.admissionportal.repository.StaffRepository;
import com.demo.admissionportal.repository.StudentRepository;
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

import java.util.Optional;

/**
 * The type Application config.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;

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
        throw new UsernameNotFoundException("Không tìm thấy user");
    }
}
