package com.demo.admissionportal.service.impl.authentication;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.TokenType;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Staff;
import com.demo.admissionportal.entity.StaffToken;
import com.demo.admissionportal.repository.StaffRepository;
import com.demo.admissionportal.repository.StaffTokenRepository;
import com.demo.admissionportal.service.AuthenticationStaffService;
import com.demo.admissionportal.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationStaffServiceImpl implements AuthenticationStaffService {
    private final AuthenticationManager authenticationManager;
    private final StaffRepository staffRepository;
    private final StaffTokenRepository staffTokenRepository;
    private final JwtService jwtService;

    @Override
    public ResponseData<LoginResponseDTO> login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var staff = staffRepository.findByUsername(request.getUsername())
                    .or(() -> Optional.ofNullable(staffRepository.findByEmail(request.getUsername())))
                    .orElseThrow(null);
            if (staff == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");
            }
            var jwtToken = jwtService.generateToken(staff);
            var refreshToken = jwtService.generateRefreshToken(staff);
            revokeAllStaffTokens(staff);
            saveStaffToken(staff, jwtToken, refreshToken);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đăng nhập thành công", LoginResponseDTO.builder().accessToken(jwtToken).refreshToken(refreshToken).build());
        } catch (Exception ex) {
            // Case 1: Bad Credential: Authentication Failure: 401
            // Case 2: Access Denied : Authorization Error: 403
            log.error("Error occurred while login: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C203.getCode(), "Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }

    private void revokeAllStaffTokens(Staff staff) {
        var validStaffTokens = staffTokenRepository.findAllValidTokenByStaff(staff.getId());
        if (validStaffTokens.isEmpty()) return;
        validStaffTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        staffTokenRepository.saveAll(validStaffTokens);
    }

    private void saveStaffToken(Staff staff, String jwtToken, String refreshToken) {
        var token = StaffToken.builder()
                .staff(staff)
                .staffToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .refreshTokenStaffToken(refreshToken)
                .build();
        staffTokenRepository.save(token);
    }
}
