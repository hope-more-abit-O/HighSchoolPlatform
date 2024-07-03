package com.demo.admissionportal.config.authentication.config;

import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.UserService;
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
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * User details service user details service.
     *
     * @return the user details service
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> studentDetails = userRepository.findByUsername(username).or(() -> userRepository.findByEmail(username));
            if (studentDetails.isPresent()) {
                return studentDetails.get();
            }
            throw new UsernameNotFoundException("Không tìm thấy user");
        };
    }

    //    /**
//     * Authentication provider authentication provider.
//     *
//     * @return the authentication provider
//     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
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
        Optional<User> userDetails = userRepository.findByUsername(username).or(() -> userRepository.findByEmail(username));
        return userDetails.orElse(null);
    }
}
