package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.TokenType;
import com.demo.admissionportal.dto.request.AuthenticationRequest;
import com.demo.admissionportal.dto.request.RegisterRequest;
import com.demo.admissionportal.dto.response.AuthenticationResponse;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.entity.Token;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.TokenRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.AuthenticationService;
import com.demo.admissionportal.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type Authentication service.
 */
@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .build();
        // Save user in DB
        var createUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(createUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .fToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        // Save token in DB
        tokenRepository.save(token);
    }
}
