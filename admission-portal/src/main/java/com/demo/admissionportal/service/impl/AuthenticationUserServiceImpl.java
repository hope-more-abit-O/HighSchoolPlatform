package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.*;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.authen.ChangePasswordRequestDTO;
import com.demo.admissionportal.dto.request.authen.CodeVerifyAccountRequestDTO;
import com.demo.admissionportal.dto.request.authen.EmailRequestDTO;
import com.demo.admissionportal.dto.request.authen.RegisterUserRequestDTO;
import com.demo.admissionportal.dto.request.redis.RegenerateOTPRequestDTO;
import com.demo.admissionportal.dto.request.redis.VerifyAccountRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.authen.*;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.AuthenticationUserService;
import com.demo.admissionportal.service.JwtService;
import com.demo.admissionportal.service.OTPService;
import com.demo.admissionportal.service.ValidationService;
import com.demo.admissionportal.util.impl.EmailUtil;
import com.demo.admissionportal.util.impl.OTPUtil;
import com.demo.admissionportal.util.impl.RandomCodeGeneratorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private final OTPUtil otpUtil;
    private final EmailUtil emailUtil;
    private final OTPService otpService;
    private final ValidationService validationService;
    private final RandomCodeGeneratorUtil randomCodeGeneratorUtil;
    private final StaffInfoRepository staffInfoRepository;
    private final UniversityInfoRepository universityInfoRepository;
    private final ConsultantInfoRepository consultantInfoRepository;
    private final WardRepository wardRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private String IMAGE = "https://firebasestorage.googleapis.com/v0/b/highschoolvn-dev.appspot.com/o/uploads%2Fstudent.png?alt=media&token=c13af7ac-f8c3-4eac-b98a-6e9dd9a191fd";

    @Override
    public ResponseData<LoginResponseDTO> login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var user = userRepository.findByUsername(request.getUsername())
                    .or(() -> userRepository.findByEmail(request.getUsername()))
                    .orElseThrow(null);
            if (user == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Bad request");
            } else if (user.getStatus().equals(AccountStatus.INACTIVE)) {
                return new ResponseData<>(ResponseCode.C209.getCode(), "Tài khoản đã bị khoá trong hệ thống");
            }
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            // TODO: Handle multiple device access account
//            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken, refreshToken);
            if (user.getRole() == Role.ADMIN && (request.getUsername().equals(user.getUsername()) || request.getUsername().equals(user.getEmail()))
                    && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new ResponseData<>(ResponseCode.C200.getCode(), "Đăng nhập thành công", LoginResponseDTO.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .user(modelMapper.map(user, LoginResponseDTO.UserLoginResponseDTO.class))
                        .build());
            } else {
                var userInfo = userInfoRepository.findUserInfoById(user.getId());
                var staffInfo = staffInfoRepository.findStaffInfoById(user.getId());
                var universityInfo = universityInfoRepository.findUniversityInfoById(user.getId());
                var consultantInfo = consultantInfoRepository.findConsultantInfoById(user.getId());

                // Initialize response DTOs
                UserInfoResponseDTO userInfoResponseDTO = null;
                StaffInfoResponseDTO staffInfoResponseDTO = null;
                UniversityInfoResponseDTO universityInfoResponseDTO = null;
                ConsultantInfoResponseDTO consultantInfoResponseDTO = null;

                // Map userInfo with custom info
                if (userInfo != null) {
                    userInfoResponseDTO = modelMapper.map(userInfo, UserInfoResponseDTO.class);
                }
                if (staffInfo != null) {
                    staffInfoResponseDTO = modelMapper.map(staffInfo, StaffInfoResponseDTO.class);
                    staffInfoResponseDTO.setAdminId(staffInfo.getAdminId());
                }

                if (universityInfo != null) {
                    universityInfoResponseDTO = modelMapper.map(universityInfo, UniversityInfoResponseDTO.class);
                }
                if (consultantInfo != null) {
                    consultantInfoResponseDTO = modelMapper.map(consultantInfo, ConsultantInfoResponseDTO.class);
                }
                return new ResponseData<>(ResponseCode.C200.getCode(), "Đăng nhập thành công", LoginResponseDTO.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .user(modelMapper.map(user, LoginResponseDTO.UserLoginResponseDTO.class))
                        .userInfo(userInfoResponseDTO)
                        .staffInfo(staffInfoResponseDTO)
                        .universityInfo(universityInfoResponseDTO)
                        .consultantInfo(consultantInfoResponseDTO)
                        .build());
            }

        } catch (Exception ex) {
            // Case 1: Bad Credential: Authentication Failure: 401
            // Case 2: Access Denied : Authorization Error: 403
            log.error("Error occurred while login: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Tên đăng nhập hoặc mật khẩu không đúng");
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

    private void saveUserToken(User user, String jwtToken, String refreshToken) {
        var token = UserToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .refreshToken(refreshToken)
                .build();
        userTokenRepository.save(token);
    }

    @Override
    public ResponseData<?> register(RegisterUserRequestDTO request) {
        try {
            String otp = otpUtil.generateOTP();
            validationService.validateRegister(request.getUsername(), request.getEmail(), request.getPhone());
            if (request.getProvider().equals(ProviderType.SYSTEM.name())) {
                // Sending OTP to Email

                if (!emailUtil.sendOtpEmail(request.getEmail(), otp)) {
                    throw new RuntimeException("Không thể gửi OTP xin vui lòng thử lại");
                }
                // Map OTP in Redis Cache
                otpService.saveOTP(request.getEmail(), otp, LocalDateTime.now());

                Ward ward = wardRepository.findWardById(request.getWard_id());
                District district = districtRepository.findDistrictById(request.getDistrict_id());
                Province province = provinceRepository.findProvinceById(request.getProvince_id());

                // Map user table
                User user = modelMapper.map(request, User.class);
                user.setAvatar(IMAGE);
                user.setRole(Role.USER);
                user.setStatus(AccountStatus.ACTIVE);
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setCreateTime(new Date());
                user.setProvider(ProviderType.SYSTEM);

                // Map user_info table
                UserInfo userInfo = modelMapper.map(request, UserInfo.class);
                userInfo.setWard(ward);
                userInfo.setDistrict(district);
                userInfo.setProvince(province);

                // Save student in Redis Cache
                otpService.saveUser(request.getEmail(), user, userInfo);
                CodeVerifyAccountRequestDTO codeVerifyAccountRequestDTO = new CodeVerifyAccountRequestDTO();

                // Generate sUID
                codeVerifyAccountRequestDTO.setSUID(randomCodeGeneratorUtil.generateRandomString());
                // Save sUID in Redis
                otpService.savesUID(request.getEmail(), codeVerifyAccountRequestDTO);

                return new ResponseData<>(ResponseCode.C206.getCode(), "Đã gửi OTP vào Email. Xin vui lòng kiểm tra", codeVerifyAccountRequestDTO);
            } else if (request.getProvider().equals(ProviderType.GOOGLE.name())) {
                User user = modelMapper.map(request, User.class);
                user.setAvatar(IMAGE);
                user.setRole(Role.USER);
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setCreateTime(new Date());
                user.setProvider(ProviderType.GOOGLE);
                user.setStatus(AccountStatus.ACTIVE);
                var createUser = userRepository.save(user);

                Ward ward = wardRepository.findWardById(request.getWard_id());
                District district = districtRepository.findDistrictById(request.getDistrict_id());
                Province province = provinceRepository.findProvinceById(request.getProvince_id());

                UserInfo userInfo = modelMapper.map(request, UserInfo.class);
                userInfo.setId(createUser.getId());
                userInfo.setWard(ward);
                userInfo.setDistrict(district);
                userInfo.setProvince(province);
                userInfo.setUser(createUser);
                userInfoRepository.save(userInfo);

                LoginRequestDTO account = new LoginRequestDTO();
                account.setUsername(request.getUsername());
                account.setPassword(request.getPassword());

                var loginAccount = login(account);

                return new ResponseData<>(ResponseCode.C206.getCode(), "Đã đăng ký thành công", loginAccount);
            }

            // Map OTP in Redis Cache
            otpService.saveOTP(request.getEmail(), otp, LocalDateTime.now());

            // Map user table
            User user = modelMapper.map(request, User.class);
            user.setRole(Role.USER);
            user.setStatus(AccountStatus.ACTIVE);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCreateTime(new Date());
            user.setProvider(ProviderType.SYSTEM);

            // Map user_info table
            UserInfo userInfo = modelMapper.map(request, UserInfo.class);

            // Save student in Redis Cache
            otpService.saveUser(request.getEmail(), user, userInfo);

            return new ResponseData<>(ResponseCode.C206.getCode(), "Đã gửi OTP vào Email. Xin vui lòng kiểm tra");

        } catch (DataExistedException de) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Username hoặc email, số điện thoại đã tồn tại");
        } catch (Exception ex) {
            log.error("Error occurred while register: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi tạo tài khoản", ex.getMessage());
        }
    }

    @Override
    public ResponseData<?> verifyAccount(VerifyAccountRequestDTO verifyAccountRequestDTO) {
        try {
            String storedOtp = otpService.getOTP(verifyAccountRequestDTO.getEmail());
            LocalDateTime storeLocalDateTime = otpService.getOTPDateTime(verifyAccountRequestDTO.getEmail());
            if (storedOtp == null) {
                return new ResponseData<>(ResponseCode.C201.getCode(), "OTP đã hết hạn không tồn tại");
            }
            if (!storedOtp.equals(verifyAccountRequestDTO.getOtpFromEmail())) {
                return new ResponseData<>(ResponseCode.C201.getCode(), "OTP không hợp lệ");
            }
            // Setting 10 minutes for OTP
            if (Duration.between(storeLocalDateTime, LocalDateTime.now()).getSeconds() < 10 * 60) {

                // Get data from Redis Cache
                User userFromRedis = otpService.getUser(verifyAccountRequestDTO.getEmail());
                UserInfo userInfoFromRedis = otpService.getUserInfo(verifyAccountRequestDTO.getEmail());

                validationService.validateRegister(userFromRedis.getUsername(), userFromRedis.getEmail(), userInfoFromRedis.getPhone());
                // Save data in DB
                var createUser = userRepository.save(userFromRedis);
                userInfoFromRedis.setId(createUser.getId());
                userInfoFromRedis.setUser(createUser);
                userInfoRepository.save(userInfoFromRedis);
                log.info("User has been verified: {}", createUser);
                otpService.deleteOTP(createUser.getEmail());
                return new ResponseData<>(ResponseCode.C200.getCode(), "Tài khoản đã được xác thực thành công");
            } else {
                return new ResponseData<>(ResponseCode.C201.getCode(), "OTP đã hết hạn");
            }
        } catch (DataExistedException de) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Tài khoản đã tồn tại trong hệ thống!");
        } catch (Exception ex) {
            log.error("Error occurred while verify: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi tạo tài khoản", ex.getMessage());
        }
    }

    @Override
    public ResponseData<?> regenerateOtp(RegenerateOTPRequestDTO requestDTO) {
        try {
            //Get new OTP
            String newOTP = otpUtil.generateOTP();

            // Get OTP from Redis
            String otpFromRedis = otpService.getOTP(requestDTO.getEmail());
            otpFromRedis = newOTP;

            otpService.deleteOTP(requestDTO.getEmail());
            // Store new OTP in Redis Cache
            otpService.saveOTP(requestDTO.getEmail().trim(), otpFromRedis, LocalDateTime.now());

            if (!emailUtil.sendOtpEmail(requestDTO.getEmail().trim(), newOTP)) {
                throw new RuntimeException("Không thể gửi OTP xin vui lòng thử lại");
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã gửi lại mã OTP. Vui lòng kiểm tra mail!");
        } catch (Exception ex) {
            log.error("regenerateOtp error: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), ex.getMessage());
        }
    }

    @Override
    public ResponseData<?> changePassword(ChangePasswordRequestDTO changePasswordRequestDTO, Principal principal) {
        try {
            var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

            // Check if the current password is correct
            if (!passwordEncoder.matches(changePasswordRequestDTO.getCurrentPassword(), user.getPassword())) {
                return new ResponseData<>(ResponseCode.C201.getCode(), "Sai mật khẩu");
            }
            // Check if the two password is the same
            if (!changePasswordRequestDTO.getNewPassword().equals(changePasswordRequestDTO.getConfirmPassword())) {
                return new ResponseData<>(ResponseCode.C201.getCode(), "Mật khẩu không giống nhau");
            }
            if (changePasswordRequestDTO.getNewPassword().equals(changePasswordRequestDTO.getCurrentPassword())) {
                return new ResponseData<>(ResponseCode.C201.getCode(), "Không được nhập mật khẩu cũ");
            }
            user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
            userRepository.save(user);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đổi mật khẩu thành công");
        } catch (Exception ex) {
            log.error("changePassword error: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lôỗi khi đổi mật khẩu");
    }

    @Override
    public ResponseData<User> checkEmailExisted(EmailRequestDTO requestDTO) {
        try {
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }
            var account = userRepository.findUserByEmail(requestDTO.getEmail());
            if (account == null) {
                return new ResponseData<>(ResponseCode.C206.getCode(), "User không tồn tại trong hệ thống. Hãy đăng ký!");
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "User tồn tại trong hệ thống", account);

        } catch (Exception ex) {
            log.error("checkEmailExisted error: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), ex.getMessage());
        }
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String oldRefreshToken;
        final String userName;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        oldRefreshToken = authHeader.substring(7);
        userName = jwtService.extractUsername(oldRefreshToken);
        if (userName != null) {
            var user = this.userRepository.findFirstByUsername(userName)
                    .orElseThrow();
            if (jwtService.isTokenValid(oldRefreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                var newRefreshToken = jwtService.generateRefreshToken(user);
                // find old token and update it
                saveRefreshToken(user, oldRefreshToken, accessToken, newRefreshToken);
                var authResponse = LoginResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveRefreshToken(User user, String oldRefreshToken, String accessToken, String newRefreshToken) {
        UserToken token = userTokenRepository.findUserTokenByRefreshToken(oldRefreshToken);
        token.setUser(user);
        token.setToken(accessToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
        token.setRefreshToken(newRefreshToken);
        userTokenRepository.save(token);
    }
}