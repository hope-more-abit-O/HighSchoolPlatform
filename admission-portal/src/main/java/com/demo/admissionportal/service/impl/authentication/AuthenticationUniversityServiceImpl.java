package com.demo.admissionportal.service.impl.authentication;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.TokenType;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.University;
import com.demo.admissionportal.entity.UniversityToken;
import com.demo.admissionportal.repository.UniversityRepository;
import com.demo.admissionportal.repository.UniversityTokenRepository;
import com.demo.admissionportal.service.AuthenticationUniversityService;
import com.demo.admissionportal.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The type Authentication university service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationUniversityServiceImpl implements AuthenticationUniversityService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UniversityTokenRepository universityTokenRepository;
    private final UniversityRepository universityRepository;

    @Override
    public ResponseData<LoginResponseDTO> login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var university = universityRepository.findByUsername(request.getUsername())
                    .or(() -> Optional.ofNullable(universityRepository.findByEmail(request.getUsername())))
                    .orElseThrow(null);
            if (university == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");
            }
            var jwtToken = jwtService.generateToken(university);
            var refreshToken = jwtService.generateRefreshToken(university);
            revokeAllUniversityTokens(university);
            saveUniversityToken(university, jwtToken, refreshToken);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đăng nhập thành công", LoginResponseDTO.builder().accessToken(jwtToken).refreshToken(refreshToken).build());
        } catch (Exception ex) {
            // Case 1: Bad Credential: Authentication Failure: 401
            // Case 2: Access Denied : Authorization Error: 403
            log.error("Error occurred while login: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C203.getCode(), "Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }

    private void revokeAllUniversityTokens(University university) {
        var validStaffTokens = universityTokenRepository.findAllValidTokenByUniversity(university.getId());
        if (validStaffTokens.isEmpty()) return;
        validStaffTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        universityTokenRepository.saveAll(validStaffTokens);
    }

    private void saveUniversityToken(University university, String jwtToken, String refreshToken) {
        var token = UniversityToken.builder()
                .university(university)
                .universityToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .refreshTokenUniversityToken(refreshToken)
                .build();
        universityTokenRepository.save(token);
    }
}
