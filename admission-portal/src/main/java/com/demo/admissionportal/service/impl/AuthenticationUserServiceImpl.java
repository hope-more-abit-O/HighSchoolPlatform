package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.constants.TokenType;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.authen.RegisterUserRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserInfo;
import com.demo.admissionportal.entity.UserToken;
import com.demo.admissionportal.repository.UserInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.repository.UserTokenRepository;
import com.demo.admissionportal.service.AuthenticationUserService;
import com.demo.admissionportal.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * The type Authentication user service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationUserServiceImpl implements AuthenticationUserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;

    @Override
    public ResponseData<LoginResponseDTO> login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var user = userRepository.findByUsername(request.getUsername())
                    .or(() -> userRepository.findByEmail(request.getUsername()))
                    .orElseThrow(null);
            if (user == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");
            }
            var jwtToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đăng nhập thành công", LoginResponseDTO.builder().accessToken(jwtToken).build());
        } catch (Exception ex) {
            // Case 1: Bad Credential: Authentication Failure: 401
            // Case 2: Access Denied : Authorization Error: 403
            log.error("Error occurred while login: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C203.getCode(), "Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = userTokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        userTokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = UserToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        userTokenRepository.save(token);
    }


    @Override
    public ResponseData<RegisterUserRequestDTO> register(RegisterUserRequestDTO request) {
        try {
            var checkExisted = userRepository.findByEmail(request.getEmail())
                    .or(() -> userRepository.findByUsername(request.getUsername()))
                    .orElse(null);
            if (checkExisted != null) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Tài khoản hoặc email đã tồn tại");
            }
            // Insert user table
            User user = modelMapper.map(request, User.class);
            user.setRole(Role.USER);
            user.setStatus(AccountStatus.ACTIVE.name());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCreateTime(new Date());
            User savedUser = userRepository.save(user);

            //Insert user_info table
            UserInfo userInfo = modelMapper.map(request, UserInfo.class);
            userInfo.setId(savedUser.getId());
            userInfoRepository.save(userInfo);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo tài khoản thành công", request);
        } catch (Exception ex) {
            log.error("Error occurred while register: {}", ex.getMessage());
            ex.printStackTrace();
        }
        return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi tạo tài khoản", null);
    }
}
