package com.demo.admissionportal.service.impl.authentication;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.TokenType;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Consultant;
import com.demo.admissionportal.entity.ConsultantToken;
import com.demo.admissionportal.repository.ConsultantRepository;
import com.demo.admissionportal.repository.ConsultantTokenRepository;
import com.demo.admissionportal.service.AuthenticationConsultantService;
import com.demo.admissionportal.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationConsultantServiceImpl implements AuthenticationConsultantService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ConsultantTokenRepository consultantTokenRepository;
    private final ConsultantRepository consultantRepository;

    @Override
    public ResponseData<LoginResponseDTO> login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var consultant = consultantRepository.findByUsername(request.getUsername())
                    .or(() -> consultantRepository.findByEmail(request.getUsername()))
                    .orElseThrow(null);
            if (consultant == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");
            }
            var jwtToken = jwtService.generateToken(consultant);
            var refreshToken = jwtService.generateRefreshToken(consultant);
            revokeAllConsultantTokens(consultant);
            saveConsultantToken(consultant, jwtToken, refreshToken);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đăng nhập thành công", LoginResponseDTO.builder().accessToken(jwtToken).refreshToken(refreshToken).build());
        } catch (Exception ex) {
            // Case 1: Bad Credential: Authentication Failure: 401
            // Case 2: Access Denied : Authorization Error: 403
            log.error("Error occurred while login: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C203.getCode(), "Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }

    private void revokeAllConsultantTokens(Consultant consultant) {
        var validStaffTokens = consultantTokenRepository.findAllValidTokenByConsultant(consultant.getId());
        if (validStaffTokens.isEmpty()) return;
        validStaffTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        consultantTokenRepository.saveAll(validStaffTokens);
    }

    private void saveConsultantToken(Consultant consultant, String jwtToken, String refreshToken) {
        var token = ConsultantToken.builder()
                .consultant(consultant)
                .consultantToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .refreshTokenConsultantToken(refreshToken)
                .build();
        consultantTokenRepository.save(token);
    }
}
