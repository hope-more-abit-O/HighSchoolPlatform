package com.demo.admissionportal.service.impl.authentication;

import com.demo.admissionportal.constants.TokenType;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Admin;
import com.demo.admissionportal.entity.AdminToken;
import com.demo.admissionportal.repository.AdminRepository;
import com.demo.admissionportal.repository.AdminTokenRepository;
import com.demo.admissionportal.service.AuthenticationAdminService;
import com.demo.admissionportal.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The type Authentication admin service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationAdminServiceImpl implements AuthenticationAdminService {
    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final AdminTokenRepository adminTokenRepository;
    private final JwtService jwtService;

    @Override
    public ResponseData<LoginResponseDTO> login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var admin = adminRepository.findByUsername(request.getUsername())
                    .or(() -> Optional.ofNullable(adminRepository.findByEmail(request.getUsername())))
                    .orElseThrow(null);
            if (admin == null) {
                return new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Không tìm thấy user");
            }
            var jwtToken = jwtService.generateToken(admin);
            var refreshToken = jwtService.generateRefreshToken(admin);
            revokeAllAdminTokens(admin);
            saveAdminToken(admin, jwtToken, refreshToken);
            return new ResponseData<>(HttpStatus.OK.value(), "Đăng nhập thành công", LoginResponseDTO.builder().accessToken(jwtToken).refreshToken(refreshToken).build());
        } catch (Exception ex) {
            // Case 1: Bad Credential: Authentication Failure: 401
            // Case 2: Access Denied : Authorization Error: 403
            log.error("Error occurred while login: {}", ex.getMessage());
            return new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }

    private void revokeAllAdminTokens(Admin admin) {
        var validStaffTokens = adminTokenRepository.findAllValidTokenByAdmin(admin.getId());
        if (validStaffTokens.isEmpty()) return;
        validStaffTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        adminTokenRepository.saveAll(validStaffTokens);
    }

    private void saveAdminToken(Admin admin, String jwtToken, String refreshToken) {
        var token = AdminToken.builder()
                .admin(admin)
                .adminToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .refreshTokenAdminToken(refreshToken)
                .build();
        adminTokenRepository.save(token);
    }
}
