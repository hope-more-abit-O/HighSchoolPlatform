package com.demo.admissionportal.service.impl.authentication;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.constants.TokenType;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.RegenerateOTPRequestDTO;
import com.demo.admissionportal.dto.request.RegisterStudentRequestDTO;
import com.demo.admissionportal.dto.request.VerifyStudentRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Student;
import com.demo.admissionportal.entity.StudentToken;
import com.demo.admissionportal.repository.StudentRepository;
import com.demo.admissionportal.repository.StudentTokenRepository;
import com.demo.admissionportal.service.AuthenticationStudentService;
import com.demo.admissionportal.service.JwtService;
import com.demo.admissionportal.service.OTPService;
import com.demo.admissionportal.util.EmailUtil;
import com.demo.admissionportal.util.OTPUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


/**
 * The type Authentication student service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationStudentServiceImpl implements AuthenticationStudentService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StudentRepository studentRepository;
    private final StudentTokenRepository studentTokenRepository;
    private final OTPUtil otpUtil;
    private final EmailUtil emailUtil;
    private final OTPService otpService;

    @Override
    public ResponseData<LoginResponseDTO> register(RegisterStudentRequestDTO request) {
        try {
            if (request == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Request bị trống");
            }
            // Case 1 : Existed By Email
            Student checkExistedByEmail = studentRepository.findByEmail(request.getEmail().trim());
            if (checkExistedByEmail != null) {
                log.error("Email {} is already existed", checkExistedByEmail.getEmail());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Email đã được tài khoản khác sử dụng");
            }
            // Case 2: Existed By UserName
            Optional<Student> checkExistedByUserName = studentRepository.findByUsername(request.getUsername());
            if (checkExistedByUserName.isPresent()) {
                log.error("Username {} is already existed", request.getUsername());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Username đã được tài khoản khác sử dụng", null);
            }
            // Case 3: Existed By Phone
            Student checkExistedByPhone = studentRepository.findByPhone(request.getPhone().trim());
            if (checkExistedByPhone != null) {
                log.error("Phone {} is already existed", checkExistedByPhone.getPhone());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoại đã được tài khoản khác sử dụng", null);
            }

            // Sending OTP to Email
            String otp = otpUtil.generateOTP();
            if (!emailUtil.sendOtpEmail(request.getEmail(), otp)) {
                throw new RuntimeException("Không thể gửi OTP xin vui lòng thử lại");
            }

            //Save OTP in Redis Cache
            otpService.saveOTP(request.getEmail(), otp, LocalDateTime.now());


            // Map request to Student
            Student student = Student.builder()
                    .username(request.getUsername().trim())
                    .firstname(request.getFirstname().trim())
                    .middleName(request.getMiddle_name().trim())
                    .lastName(request.getLastname().trim())
                    .email(request.getEmail().trim())
                    .password(passwordEncoder.encode(request.getPassword()).trim())
                    .addressId(request.getAddress())
                    .birthday(request.getBirthday())
                    .educationLevel(request.getEducationLevel())
                    .avatar(request.getAvatar())
                    .gender(request.getGender())
                    .role(Role.STUDENT)
                    .phone(request.getPhone())
                    .status(AccountStatus.ACTIVE.name())
                    .build();

            // Save student in Redis Cache
            otpService.saveStudent(student.getEmail(), student);

            return new ResponseData<>(ResponseCode.C206.getCode(), "Đã gửi OTP vào Email. Xin vui lòng kiểm tra");
        } catch (Exception ex) {
            log.error("Error occurred while sending email: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C201.getCode(), "Xuất hiện lỗi khi tạo tài khoản", null);
    }

    @Override
    public ResponseData<LoginResponseDTO> login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var student = studentRepository.findByUsername(request.getUsername())
                    .or(() -> Optional.ofNullable(studentRepository.findByEmail(request.getUsername())))
                    .orElseThrow(null);
            if (student == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy user");
            }
            var jwtToken = jwtService.generateToken(student);
            var refreshToken = jwtService.generateRefreshToken(student);
            revokeAllStudentTokens(student);
            saveStudentToken(student, jwtToken, refreshToken);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đăng nhập thành công", LoginResponseDTO.builder().accessToken(jwtToken).refreshToken(refreshToken).build());
        } catch (Exception ex) {
            // Case 1: Bad Credential: Authentication Failure: 401
            // Case 2: Access Denied : Authorization Error: 403
            log.error("Error occurred while login: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C203.getCode(), "Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }

    private void saveStudentToken(Student student, String jwtToken, String refreshToken) {
        var token = StudentToken.builder().student(student).tokenStudent(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false).refreshTokenStudent(refreshToken).build();
        studentTokenRepository.save(token);
    }

    private void revokeAllStudentTokens(Student student) {
        var validUserTokens = studentTokenRepository.findAllValidTokenByUser(student.getId());
        if (validUserTokens.isEmpty()) return;
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
            var student = studentRepository.findByUsername(username).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, student)) {
                var accessToken = jwtService.generateToken(student);
                revokeAllStudentTokens(student);
                saveStudentToken(student, accessToken, refreshToken);
                var authResponse = LoginResponseDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public ResponseData<?> verifyAccount(VerifyStudentRequestDTO verifyStudentRequestDTO) {
        String storedOtp = otpService.getOTP(verifyStudentRequestDTO.getEmail());
        LocalDateTime storeLocalDateTime = otpService.getOTPDateTime(verifyStudentRequestDTO.getEmail());
        if (storedOtp == null) {
            return new ResponseData<>(ResponseCode.C201.getCode(), "OTP đã hết hạn không tồn tại");
        }
        if (!storedOtp.equals(verifyStudentRequestDTO.getOtpFromEmail())) {
            return new ResponseData<>(ResponseCode.C201.getCode(), "OTP không hợp lệ");
        }
        // Setting 10 minutes for OTP
        if (Duration.between(storeLocalDateTime, LocalDateTime.now()).getSeconds() < 10 * 60) {
            // Get data from Redis Cache
            Student studentFromRedis = otpService.getStudent(verifyStudentRequestDTO.getEmail());

            // Save data in DB
            var createStudent = studentRepository.save(studentFromRedis);

            var jwtToken = jwtService.generateToken(createStudent);
            var refreshToken = jwtService.generateRefreshToken(createStudent);
            saveStudentToken(createStudent, jwtToken, refreshToken);
            log.info("Student has been verified: {}", createStudent);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tài khoản đã được xác thực thành công", LoginResponseDTO.builder().accessToken(jwtToken).refreshToken(refreshToken).build());
        } else {
            return new ResponseData<>(ResponseCode.C201.getCode(), "OTP đã hết hạn");
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
}
