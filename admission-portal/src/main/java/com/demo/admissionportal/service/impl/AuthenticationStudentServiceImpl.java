package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.constants.TokenType;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.RegisterStudentRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Student;
import com.demo.admissionportal.entity.StudentToken;
import com.demo.admissionportal.repository.StudentRepository;
import com.demo.admissionportal.repository.StudentTokenRepository;
import com.demo.admissionportal.service.AuthenticationStudentService;
import com.demo.admissionportal.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * The type Authentication student service.
 */
@Service
@AllArgsConstructor
public class AuthenticationStudentServiceImpl implements AuthenticationStudentService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StudentRepository studentRepository;
    private final StudentTokenRepository studentTokenRepository;

    @Override
    public ResponseData<LoginResponseDTO> register(RegisterStudentRequestDTO request) {
        var student = Student.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .middleName(request.getMiddle_name())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .addressId(request.getAddress())
                .birthday(request.getBirthday())
                .educationLevel(request.getEducationLevel())
                .avatar(request.getAvatar())
                .gender(request.getGender())
                .role(Role.STUDENT)
                .phone(request.getPhone())
                .status("ACTIVE")
                .build();
        // Save user in DB
        var createStudent = studentRepository.save(student);
        var jwtToken = jwtService.generateToken(student);
        var refreshToken = jwtService.generateRefreshToken(student);
        saveStudentToken(createStudent, jwtToken, refreshToken);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Tạo tài khoản thành công",
                LoginResponseDTO.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .build());
    }

    @Override
    public ResponseData<LoginResponseDTO> login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var student = studentRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(student);
        var refreshToken = jwtService.generateRefreshToken(student);
        revokeAllStudentTokens(student);
        saveStudentToken(student, jwtToken, refreshToken);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Đăng nhập thành công",
                LoginResponseDTO.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .build());
    }

    private void saveStudentToken(Student student, String jwtToken, String refreshToken) {
        var token = StudentToken.builder()
                .student(student)
                .tokenStudent(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .refreshTokenStudent(refreshToken)
                .build();
        // Save token in DB
        studentTokenRepository.save(token);
    }

    private void revokeAllStudentTokens(Student student) {
        var validUserTokens = studentTokenRepository.findAllValidTokenByUser(student.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        studentTokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var student = studentRepository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, student)) {
                var accessToken = jwtService.generateToken(student);
                revokeAllStudentTokens(student);
                saveStudentToken(student, accessToken, refreshToken);
                var authResponse = LoginResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
