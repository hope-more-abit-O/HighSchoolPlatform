package com.demo.admissionportal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.demo.admissionportal.constants.*;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.authen.ChangePasswordRequestDTO;
import com.demo.admissionportal.dto.request.authen.CodeVerifyAccountRequestDTO;
import com.demo.admissionportal.dto.request.authen.EmailRequestDTO;
import com.demo.admissionportal.dto.request.authen.RegisterUserRequestDTO;
import com.demo.admissionportal.dto.request.redis.RegenerateOTPRequestDTO;
import com.demo.admissionportal.dto.request.redis.VerifyAccountRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.authen.ConsultantInfoResponseDTO;
import com.demo.admissionportal.dto.response.authen.LoginResponseDTO;
import com.demo.admissionportal.dto.response.authen.StaffInfoResponseDTO;
import com.demo.admissionportal.dto.response.authen.UserInfoResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.JwtService;
import com.demo.admissionportal.service.OTPService;
import com.demo.admissionportal.service.ValidationService;
import com.demo.admissionportal.service.impl.AuthenticationUserServiceImpl;
import com.demo.admissionportal.util.impl.EmailUtil;
import com.demo.admissionportal.util.impl.OTPUtil;
import com.demo.admissionportal.util.impl.RandomCodeGeneratorUtil;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


class AuthenticationUserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTokenRepository userTokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private OTPUtil otpUtil;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private OTPService otpService;

    @Mock
    private ValidationService validationService;

    @Mock
    private RandomCodeGeneratorUtil randomCodeGeneratorUtil;

    @Mock
    private StaffInfoRepository staffInfoRepository;

    @Mock
    private UniversityInfoRepository universityInfoRepository;

    @Mock
    private ConsultantInfoRepository consultantInfoRepository;

    @Mock
    private WardRepository wardRepository;

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private ProvinceRepository provinceRepository;

    @Captor
    ArgumentCaptor<LocalDateTime> localDateTimeCaptor;


    @InjectMocks
    private AuthenticationUserServiceImpl authenticationUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginUserSuccess() {
        LoginRequestDTO request = new LoginRequestDTO("user@example.com", "password");

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
        user.setStatus(AccountStatus.ACTIVE);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())))
                .thenReturn(null);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        ResponseData<LoginResponseDTO> response = authenticationUserService.login(request);

        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("Đăng nhập thành công", response.getMessage());
    }

    @Test
    void testLoginAdminSuccess() {
        LoginRequestDTO request = new LoginRequestDTO("admin@example.com", "adminPassword");

        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.ADMIN);
        user.setStatus(AccountStatus.ACTIVE);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())))
                .thenReturn(null);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        ResponseData<LoginResponseDTO> response = authenticationUserService.login(request);

        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("Đăng nhập thành công", response.getMessage());
    }
    @Test
    void testLoginStaffSuccess() {
        // Prepare the login request
        LoginRequestDTO request = new LoginRequestDTO("university@example.com", "universityPassword");

        // Mock User entity
        User user = new User();
        user.setId(1); // Ensure ID is set
        user.setUsername("university");
        user.setEmail("university@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.STAFF);
        user.setStatus(AccountStatus.ACTIVE);

        // Mock StaffInfo entity
        StaffInfo staffInfo = new StaffInfo();
        staffInfo.setAdminId(3);
        staffInfo.setFirstName("Hieu");
        staffInfo.setLastName("Bui");
        staffInfo.setPhone("0369776640");

        // Mock StaffInfoResponseDTO
        StaffInfoResponseDTO staffInfoResponseDTO = new StaffInfoResponseDTO();
        staffInfoResponseDTO.setAdminId(3);
        staffInfoResponseDTO.setFirstName("Hieu");
        staffInfoResponseDTO.setLastName("Bui");
        staffInfoResponseDTO.setPhone("0369776640");

        // Mock authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);  // Simulate successful authentication

        // Mock repository calls
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(staffInfoRepository.findStaffInfoById(user.getId())).thenReturn(staffInfo);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Mock ModelMapper to map StaffInfo to StaffInfoResponseDTO
        when(modelMapper.map(staffInfo, StaffInfoResponseDTO.class)).thenReturn(staffInfoResponseDTO);

        // Mock JWT generation
        String jwtToken = "mockJwtToken";
        String refreshToken = "mockRefreshToken";
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

        // Perform the login
        ResponseData<LoginResponseDTO> response = authenticationUserService.login(request);

        // Assertions
        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("Đăng nhập thành công", response.getMessage());

        // Ensure the response data is not null
        assertNotNull(response.getData(), "Login Response Data is null");
        assertNotNull(response.getData().getAccessToken(), "Access Token is null");
        assertNotNull(response.getData().getRefreshToken(), "Refresh Token is null");

        // Ensure the staff info is correctly mapped
        assertNotNull(response.getData().getStaffInfo(), "Staff Info is null");
        assertEquals(3, response.getData().getStaffInfo().getAdminId());
        assertEquals("Hieu", response.getData().getStaffInfo().getFirstName());
        assertEquals("Bui", response.getData().getStaffInfo().getLastName());
        assertEquals("0369776640", response.getData().getStaffInfo().getPhone());
    }

    @Test
    void testLoginWithConsultantInfo() {
        // Prepare the login request
        LoginRequestDTO request = new LoginRequestDTO("consultant@example.com", "consultantPassword");

        // Mock User entity
        User user = new User();
        user.setId(2); // Ensure ID is set
        user.setUsername("consultant");
        user.setEmail("consultant@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.CONSULTANT);
        user.setStatus(AccountStatus.ACTIVE);

        // Mock ConsultantInfo entity
        ConsultantInfo consultantInfo = new ConsultantInfo();
        consultantInfo.setUniversityId(1);
        consultantInfo.setFirstName("zxczxc");
        consultantInfo.setLastName("zxczxc");
        consultantInfo.setMiddleName("zxczxc");

        // Mock ConsultantInfoResponseDTO
        ConsultantInfoResponseDTO consultantInfoResponseDTO = new ConsultantInfoResponseDTO();
        consultantInfoResponseDTO.setUniversityId(1);
        consultantInfoResponseDTO.setFirstname("zxczxc");
        consultantInfoResponseDTO.setLastName("zxczxc");
        consultantInfoResponseDTO.setMiddleName("zxczxc");

        // Mock University entity
        User universityUser = new User();
        universityUser.setId(1);
        universityUser.setAvatar("university-avatar-url");

        UniversityInfo universityInfo = new UniversityInfo();
        universityInfo.setId(1);
        universityInfo.setName("University Name");

        // Mock authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);  // Simulate successful authentication

        // Mock repository calls
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(consultantInfoRepository.findConsultantInfoById(user.getId())).thenReturn(consultantInfo);
        when(userRepository.findUserById(consultantInfoResponseDTO.getUniversityId())).thenReturn(universityUser);
        when(universityInfoRepository.findUniversityInfoById(universityUser.getId())).thenReturn(universityInfo);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Mock ModelMapper to map ConsultantInfo to ConsultantInfoResponseDTO
        when(modelMapper.map(consultantInfo, ConsultantInfoResponseDTO.class)).thenReturn(consultantInfoResponseDTO);

        // Manually set additional fields if mapping is an issue
        consultantInfoResponseDTO.setAvatarUniversity(universityUser.getAvatar());
        consultantInfoResponseDTO.setFullNameUniversity(universityInfo.getName());

        // Mock JWT generation
        String jwtToken = "mockJwtToken";
        String refreshToken = "mockRefreshToken";
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

        // Perform the login
        ResponseData<LoginResponseDTO> response = authenticationUserService.login(request);

        // Assertions
        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("Đăng nhập thành công", response.getMessage());

        // Ensure the response data is not null
        assertNotNull(response.getData(), "Login Response Data is null");
        assertNotNull(response.getData().getAccessToken(), "Access Token is null");
        assertNotNull(response.getData().getRefreshToken(), "Refresh Token is null");

        // Ensure the consultant info is correctly mapped and populated
        assertNotNull(response.getData().getConsultantInfo(), "Consultant Info is null");
        assertEquals(1, response.getData().getConsultantInfo().getUniversityId());
        assertEquals("zxczxc", response.getData().getConsultantInfo().getFirstname());
        assertEquals("zxczxc", response.getData().getConsultantInfo().getMiddleName());
        assertEquals("zxczxc", response.getData().getConsultantInfo().getLastName());
        assertEquals("university-avatar-url", response.getData().getConsultantInfo().getAvatarUniversity());
        assertEquals("University Name", response.getData().getConsultantInfo().getFullNameUniversity());
    }



    @Test
    void testLoginUniversitySuccess() {
        LoginRequestDTO request = new LoginRequestDTO("university@example.com", "universityPassword");

        User user = new User();
        user.setUsername("university");
        user.setEmail("university@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.UNIVERSITY);
        user.setStatus(AccountStatus.ACTIVE);

        UniversityInfo universityInfo = new UniversityInfo();
        universityInfo.setName("Test University");

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())))
                .thenReturn(null);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(universityInfoRepository.findUniversityInfoById(user.getId())).thenReturn(universityInfo);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        ResponseData<LoginResponseDTO> response = authenticationUserService.login(request);

        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("Đăng nhập thành công", response.getMessage());
    }

    @Test
    void testLoginUserStatusInactive() {
        // Prepare the login request
        LoginRequestDTO request = new LoginRequestDTO("user@example.com", "password");

        // Mock User entity
        User user = new User();
        user.setStatus(AccountStatus.INACTIVE);

        // Mock userRepository to return user
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Perform the login
        ResponseData<LoginResponseDTO> response = authenticationUserService.login(request);

        // Assertions
        assertEquals(ResponseCode.C209.getCode(), response.getStatus());
        assertEquals("Tài khoản đã bị khoá trong hệ thống", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testLoginSuccessfulWithAdminRole() {
        // Prepare the login request
        LoginRequestDTO request = new LoginRequestDTO("admin@example.com", "password");

        // Mock User entity
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setEmail("admin@example.com");
        user.setRole(Role.ADMIN);
        user.setStatus(AccountStatus.ACTIVE);

        // Mock repositories
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Perform the login
        ResponseData<LoginResponseDTO> response = authenticationUserService.login(request);

        // Assertions
        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("Đăng nhập thành công", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("jwtToken", response.getData().getAccessToken());
        assertEquals("refreshToken", response.getData().getRefreshToken());
    }

    @Test
    void testLoginSuccessfulWithStaffRole() {
        // Prepare the login request
        LoginRequestDTO request = new LoginRequestDTO("staff@example.com", "password");

        // Mock User entity
        User user = new User();
        user.setId(2);
        user.setUsername("staff");
        user.setEmail("staff@example.com");
        user.setRole(Role.STAFF);
        user.setStatus(AccountStatus.ACTIVE);

        // Mock StaffInfo entity
        StaffInfo staffInfo = new StaffInfo();
        staffInfo.setAdminId(3);

        // Mock repositories
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(staffInfoRepository.findStaffInfoById(user.getId())).thenReturn(staffInfo);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");
        when(modelMapper.map(staffInfo, StaffInfoResponseDTO.class)).thenReturn(new StaffInfoResponseDTO());

        // Perform the login
        ResponseData<LoginResponseDTO> response = authenticationUserService.login(request);

        // Assertions
        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("Đăng nhập thành công", response.getMessage());
        assertNotNull(response.getData());
        assertNotNull(response.getData().getStaffInfo());
    }

    @Test
    void login_userNotFound() {
        // add request value
        LoginRequestDTO loginRequest = new LoginRequestDTO("username", "password");

        // mock data
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Call the login method
        ResponseData<LoginResponseDTO> response = authenticationUserService.login(loginRequest);

        // check the response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertEquals("Tên đăng nhập hoặc mật khẩu không đúng", response.getMessage());

        // verify that authenticate was called (since it's called before user retrieval)
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_accountInactive() {
        // add request value
        LoginRequestDTO loginRequest = new LoginRequestDTO("username", "password");
        User user = new User();
        user.setUsername("zxczxczxc");
        user.setEmail("zxczxc@gmail.com");
        user.setStatus(AccountStatus.INACTIVE);
        // mock data
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        //check response
        ResponseData<LoginResponseDTO> response = authenticationUserService.login(loginRequest);
        //expected response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertNull(response.getData());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void register_successful_systemProvider() {
        // add request value
        RegisterUserRequestDTO request = new RegisterUserRequestDTO();
        request.setUsername("zxczxc");
        request.setEmail("zxczxc@zxczxc.com");
        request.setPhone("0123456789");
        request.setPassword("zxczxczxc");
        request.setProvider(ProviderType.SYSTEM.name());
        request.setProvince_id(1);
        request.setDistrict_id(1);
        request.setWard_id(1);
        // create mock data
        String otp = "123456";
        Ward ward = new Ward();
        District district = new District();
        Province province = new Province();
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setAvatar("default.png");
        user.setStatus(AccountStatus.ACTIVE);
        // mock data
        when(otpUtil.generateOTP()).thenReturn(otp);
        when(emailUtil.sendOtpEmail(request.getEmail(), otp)).thenReturn(true);
        when(wardRepository.findWardById(request.getWard_id())).thenReturn(ward);
        when(districtRepository.findDistrictById(request.getDistrict_id())).thenReturn(district);
        when(provinceRepository.findProvinceById(request.getProvince_id())).thenReturn(province);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(modelMapper.map(request, User.class)).thenReturn(user);
        when(modelMapper.map(request, UserInfo.class)).thenReturn(new UserInfo());
        //check response
        ResponseData<?> response = authenticationUserService.register(request);
        // save local date time
        verify(otpService).saveOTP(eq(request.getEmail()), eq(otp), localDateTimeCaptor.capture());
        LocalDateTime saveDateTime = localDateTimeCaptor.getValue();
        //expected response
        assertEquals(ResponseCode.C206.getCode(), response.getStatus());
        assertEquals("Đã gửi OTP vào Email. Xin vui lòng kiểm tra", response.getMessage());
        verify(otpService).saveUser(eq(request.getEmail()), any(User.class), any(UserInfo.class));
        verify(otpService).savesUID(eq(request.getEmail()), any(CodeVerifyAccountRequestDTO.class));
        assertNotNull(saveDateTime);
    }

    @Test
    void register_emailSendFailure() {
        // Add request value
        RegisterUserRequestDTO request = new RegisterUserRequestDTO();
        request.setUsername("testuser");
        request.setEmail("testuser@example.com");
        request.setPhone("123456789");
        request.setPassword("password");
        request.setProvider(ProviderType.SYSTEM.name());

        // Create mock data
        String otp = "123456";

        // Mock the OTP generation and email sending failure
        when(otpUtil.generateOTP()).thenReturn(otp);
        when(emailUtil.sendOtpEmail(request.getEmail(), otp))
                .thenThrow(new RuntimeException("Không thể gửi OTP xin vui lòng thử lại"));

        // Call the method under test
        ResponseData<?> response = authenticationUserService.register(request);

        // Verify the response
        assertEquals(ResponseCode.C207.getCode(), response.getStatus());
        // Adjusted to match the actual message returned by the method
        assertEquals("Xuất hiện lỗi khi tạo tài khoản", response.getMessage());

        // Ensure that saveOTP and saveUser were never called due to the exception
        verify(otpService, never()).saveOTP(anyString(), anyString(), any());
        verify(otpService, never()).saveUser(anyString(), any(User.class), any(UserInfo.class));
    }

    @Test
    void register_dataExistedException() {
        // add request value
        RegisterUserRequestDTO request = new RegisterUserRequestDTO();
        request.setUsername("existingUser");
        request.setEmail("existing@example.com");
        request.setPhone("123456789");
        request.setProvider(ProviderType.SYSTEM.name());
        // create mock data
        doThrow(new DataExistedException("Username hoặc email, số điện thoại đã tồn tại"))
                .when(validationService).validateRegister(request.getUsername(), request.getEmail(), request.getPhone());
        //check response
        ResponseData<?> response = authenticationUserService.register(request);
        //expected response
        assertEquals(ResponseCode.C204.getCode(), response.getStatus());
        assertEquals("Username hoặc email, số điện thoại đã tồn tại", response.getMessage());
        verify(otpService, never()).saveOTP(anyString(), anyString(), any());
        verify(otpService, never()).saveUser(anyString(), any(User.class), any(UserInfo.class));
    }

    @Test
    void register_generalException() {
        // Prepare request data
        RegisterUserRequestDTO request = new RegisterUserRequestDTO();
        request.setUsername("testuser");
        request.setEmail("testuser@example.com");
        request.setPhone("123456789");
        request.setPassword("password");
        request.setProvider(ProviderType.SYSTEM.name());
        // create mock data response
        when(otpUtil.generateOTP()).thenThrow(new RuntimeException("Unexpected error"));
        //check response
        ResponseData<?> response = authenticationUserService.register(request);
        //expected response
        assertEquals(ResponseCode.C207.getCode(), response.getStatus());
        assertEquals("Xuất hiện lỗi khi tạo tài khoản", response.getMessage());
        assertEquals("Unexpected error", response.getData());
        verify(otpService, never()).saveOTP(anyString(), anyString(), any());
        verify(otpService, never()).saveUser(anyString(), any(User.class), any(UserInfo.class));
    }

    @Test
    void regenerateOtp_success() {
        // add request value
        RegenerateOTPRequestDTO requestDTO = new RegenerateOTPRequestDTO();
        requestDTO.setEmail("testuser@example.com");

        // create mock data
        String oldOtp = "123456";
        String newOtp = "654321";

        // mock data
        when(otpService.getOTP(requestDTO.getEmail())).thenReturn(oldOtp);
        when(otpUtil.generateOTP()).thenReturn(newOtp);
        when(emailUtil.sendOtpEmail(requestDTO.getEmail(), newOtp)).thenReturn(true);

        // check response
        ResponseData<?> response = authenticationUserService.regenerateOtp(requestDTO);

        // expected response
        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("Đã gửi lại mã OTP. Vui lòng kiểm tra mail!", response.getMessage());

        // verify method calls
        verify(otpService).deleteOTP(requestDTO.getEmail());
        verify(otpService).saveOTP(eq(requestDTO.getEmail()), eq(newOtp), any(LocalDateTime.class));
        verify(emailUtil).sendOtpEmail(requestDTO.getEmail(), newOtp);
    }


    @Test
    void regenerateOtp_emailSendFailure() {
        // Add request value
        RegenerateOTPRequestDTO requestDTO = new RegenerateOTPRequestDTO();
        requestDTO.setEmail("testuser@example.com");

        // Create mock data
        String oldOtp = "123456";
        String newOtp = "654321";

        // Mock data
        when(otpService.getOTP(requestDTO.getEmail())).thenReturn(oldOtp);
        when(otpUtil.generateOTP()).thenReturn(newOtp);

        // Throwing exception when sending OTP email
        doThrow(new RuntimeException("Không thể gửi OTP xin vui lòng thử lại"))
                .when(emailUtil).sendOtpEmail(requestDTO.getEmail(), newOtp);

        // Check response
        ResponseData<?> response = authenticationUserService.regenerateOtp(requestDTO);

        // Expected response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertEquals("Không thể gửi OTP xin vui lòng thử lại", response.getMessage());

        // Ensure that saveOTP was called with correct arguments
        verify(otpService).saveOTP(eq(requestDTO.getEmail()), eq(newOtp), any(LocalDateTime.class));
    }

    @Test
    void verifyAccount_otpNotFoundOrExpired() {
        // add request value
        VerifyAccountRequestDTO verifyAccountRequestDTO = new VerifyAccountRequestDTO("zxczxc@czxczx.com", "123456");
        // mock data
        when(otpService.getOTP(verifyAccountRequestDTO.getEmail())).thenReturn(null);
        //check response
        ResponseData<?> response = authenticationUserService.verifyAccount(verifyAccountRequestDTO);
        //expected response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertEquals("OTP đã hết hạn không tồn tại", response.getMessage());
        verify(otpService).getOTP(verifyAccountRequestDTO.getEmail());
    }

    @Test
    void changePassword_success() {
        // add request value
        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();
        changePasswordRequestDTO.setCurrentPassword("zxczxc");
        changePasswordRequestDTO.setNewPassword("zxczxczxc");
        changePasswordRequestDTO.setConfirmPassword("zxczxczxc");
        User user = new User();
        user.setPassword("zxczxc");
        // mock data
        Principal principal = mock(Principal.class);
        UsernamePasswordAuthenticationToken authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(user);
        when(principal.getName()).thenReturn("user");
        when(passwordEncoder.matches("zxczxc", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("zxczxczxc")).thenReturn("zxczxczxc");
        //check response
        ResponseData<?> response = authenticationUserService.changePassword(changePasswordRequestDTO, authenticationToken);
        //expected response
        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("Đổi mật khẩu thành công", response.getMessage());
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_incorrectCurrentPassword() {
        // add request value
        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();
        changePasswordRequestDTO.setCurrentPassword("zxczxc");
        changePasswordRequestDTO.setNewPassword("zxczxczxc");
        changePasswordRequestDTO.setConfirmPassword("zxczxczxc");

        User user = new User();
        user.setPassword("zxczxc");
        // mock data
        Principal principal = mock(Principal.class);
        UsernamePasswordAuthenticationToken authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(user);
        when(principal.getName()).thenReturn("cccc");
        when(passwordEncoder.matches("zxczxc", user.getPassword())).thenReturn(false);
        //check response
        ResponseData<?> response = authenticationUserService.changePassword(changePasswordRequestDTO, authenticationToken);
        //expected response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertEquals("Sai mật khẩu", response.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_newPasswordsDoNotMatch() {
        // add request value
        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();
        changePasswordRequestDTO.setCurrentPassword("zxczxc");
        changePasswordRequestDTO.setNewPassword("zxczxczxc");
        changePasswordRequestDTO.setConfirmPassword("zxczxczxczxc");

        User user = new User();
        user.setPassword("zxczxc");
        // mock data
        Principal principal = mock(Principal.class);
        UsernamePasswordAuthenticationToken authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(user);
        when(principal.getName()).thenReturn("dasd");
        when(passwordEncoder.matches("zxczxc", user.getPassword())).thenReturn(true);
        //check response
        ResponseData<?> response = authenticationUserService.changePassword(changePasswordRequestDTO, authenticationToken);
        //expected response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertEquals("Mật khẩu không giống nhau", response.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_newPasswordSameAsCurrentPassword() {
        // add request value
        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();
        changePasswordRequestDTO.setCurrentPassword("zxczxc");
        changePasswordRequestDTO.setNewPassword("zxczxc");
        changePasswordRequestDTO.setConfirmPassword("zxczxc");
        User user = new User();
        user.setPassword("zxczxc");
        // mock data
        Principal principal = mock(Principal.class);
        UsernamePasswordAuthenticationToken authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenReturn(user);
        when(principal.getName()).thenReturn("hihiahah");
        when(passwordEncoder.matches("zxczxc", user.getPassword())).thenReturn(true);
        //check response
        ResponseData<?> response = authenticationUserService.changePassword(changePasswordRequestDTO, authenticationToken);
        //expected response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertEquals("Không được nhập mật khẩu cũ", response.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_exceptionThrown() {
        // add request value
        ChangePasswordRequestDTO changePasswordRequestDTO = new ChangePasswordRequestDTO();
        changePasswordRequestDTO.setCurrentPassword("oldPassword");
        changePasswordRequestDTO.setNewPassword("newPassword");
        changePasswordRequestDTO.setConfirmPassword("newPassword");
        // mock data
        Principal principal = mock(Principal.class);
        UsernamePasswordAuthenticationToken authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authenticationToken.getPrincipal()).thenThrow(new RuntimeException("Unexpected error"));
        //check response
        ResponseData<?> response = authenticationUserService.changePassword(changePasswordRequestDTO, authenticationToken);
        //expected response
        assertEquals(ResponseCode.C207.getCode(), response.getStatus());
        assertEquals("Xuất hiện lôỗi khi đổi mật khẩu", response.getMessage());
        verify(userRepository, never()).save(any());
    }
    @Test
    void checkEmailExisted_success() {
        // add request value
        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setEmail("existing@example.com");
        // create mock data
        User user = new User();
        user.setEmail(requestDTO.getEmail());
        // mock data
        when(userRepository.findUserByEmail(requestDTO.getEmail())).thenReturn(user);
        // check response
        ResponseData<User> response = authenticationUserService.checkEmailExisted(requestDTO);
        // expected response
        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("User tồn tại trong hệ thống", response.getMessage());
        assertEquals(user, response.getData());
        // verify method calls
        verify(userRepository).findUserByEmail(requestDTO.getEmail());
    }
    @Test
    void refreshToken_success() throws IOException {
        // add request value
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream outputStream = mock(ServletOutputStream.class);
        String oldRefreshToken = "oldRefreshToken";
        String accessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";
        String username = "testuser";
        // create mock data
        User user = new User();
        user.setUsername(username);
        UserToken userToken = new UserToken();
        userToken.setRefreshToken(oldRefreshToken);
        // mock data
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + oldRefreshToken);
        when(jwtService.extractUsername(oldRefreshToken)).thenReturn(username);
        when(userRepository.findFirstByUsername(username)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(oldRefreshToken, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(newRefreshToken);
        when(userTokenRepository.findUserTokenByRefreshToken(oldRefreshToken)).thenReturn(userToken);
        when(response.getOutputStream()).thenReturn(outputStream);
        // execute method
        authenticationUserService.refreshToken(request, response);
        // verify method calls
        verify(userRepository).findFirstByUsername(username);
        verify(jwtService).generateToken(user);
        verify(jwtService).generateRefreshToken(user);
        verify(userTokenRepository).save(any(UserToken.class));
        verify(response).getOutputStream();
    }

    @Test
    void revokeAllUserTokens_withValidTokens_shouldExpireAndRevoke() {
        // Prepare mock data
        User user = new User();
        user.setId(1);

        UserToken validToken = new UserToken();
        validToken.setExpired(false);
        validToken.setRevoked(false);

        List<UserToken> validTokens = List.of(validToken);

        when(userTokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(validTokens);

        // Execute method
        authenticationUserService.revokeAllUserTokens(user);

        // Verify that tokens are expired and revoked
        assertTrue(validToken.isExpired());
        assertTrue(validToken.isRevoked());
        verify(userTokenRepository).saveAll(validTokens);
    }

    @Test
    void revokeAllUserTokens_withNoValidTokens_shouldDoNothing() {
        // Prepare mock data
        User user = new User();
        user.setId(1);

        when(userTokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(Collections.emptyList());

        // Execute method
        authenticationUserService.revokeAllUserTokens(user);

        // Verify that no tokens were revoked
        verify(userTokenRepository, never()).saveAll(anyList());
    }

    @Test
    void verifyAccount_otpNotMatching_shouldReturnInvalidOtpResponse() {
        // Prepare request
        VerifyAccountRequestDTO request = new VerifyAccountRequestDTO("test@example.com", "wrongOtp");

        // Mock OTP retrieval
        when(otpService.getOTP(request.getEmail())).thenReturn("correctOtp");

        // Execute method
        ResponseData<?> response = authenticationUserService.verifyAccount(request);

        // Verify response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertEquals("OTP không hợp lệ", response.getMessage());
    }

    @Test
    void verifyAccount_otpExpired_shouldReturnOtpExpiredResponse() {
        // Prepare request
        VerifyAccountRequestDTO request = new VerifyAccountRequestDTO("test@example.com", "correctOtp");

        // Mock OTP retrieval and timing
        when(otpService.getOTP(request.getEmail())).thenReturn("correctOtp");
        when(otpService.getOTPDateTime(request.getEmail())).thenReturn(LocalDateTime.now().minusMinutes(11));

        // Execute method
        ResponseData<?> response = authenticationUserService.verifyAccount(request);

        // Verify response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertEquals("OTP đã hết hạn", response.getMessage());
    }

    @Test
    void verifyAccount_invalidOtp() {
        // Setup
        VerifyAccountRequestDTO verifyAccountRequestDTO = new VerifyAccountRequestDTO("test@example.com", "wrongOtp");

        // Mock the OTP service to return a different OTP
        when(otpService.getOTP(verifyAccountRequestDTO.getEmail())).thenReturn("123456");

        // Run the verifyAccount method
        ResponseData<?> response = authenticationUserService.verifyAccount(verifyAccountRequestDTO);

        // Assert the expected response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertEquals("OTP không hợp lệ", response.getMessage());
        verify(otpService).getOTP(verifyAccountRequestDTO.getEmail());
    }


    @Test
    void verifyAccount_otpExpired() {
        // Setup
        VerifyAccountRequestDTO verifyAccountRequestDTO = new VerifyAccountRequestDTO("test@example.com", "123456");

        // Mock the OTP service to return a valid OTP
        when(otpService.getOTP(verifyAccountRequestDTO.getEmail())).thenReturn("123456");

        // Mock the OTP date time to be more than 10 minutes old
        when(otpService.getOTPDateTime(verifyAccountRequestDTO.getEmail())).thenReturn(LocalDateTime.now().minusMinutes(11));

        // Run the verifyAccount method
        ResponseData<?> response = authenticationUserService.verifyAccount(verifyAccountRequestDTO);

        // Assert the expected response
        assertEquals(ResponseCode.C201.getCode(), response.getStatus());
        assertEquals("OTP đã hết hạn", response.getMessage());
        verify(otpService).getOTP(verifyAccountRequestDTO.getEmail());
        verify(otpService).getOTPDateTime(verifyAccountRequestDTO.getEmail());
    }


    @Test
    void verifyAccount_success() {
        // Setup
        VerifyAccountRequestDTO verifyAccountRequestDTO = new VerifyAccountRequestDTO("test@example.com", "123456");

        User userFromRedis = new User();
        userFromRedis.setUsername("username");
        userFromRedis.setEmail("test@example.com");

        UserInfo userInfoFromRedis = new UserInfo();
        userInfoFromRedis.setPhone("123456789");

        // Mock the OTP service to return a valid OTP and timestamp within 10 minutes
        when(otpService.getOTP(verifyAccountRequestDTO.getEmail())).thenReturn("123456");
        when(otpService.getOTPDateTime(verifyAccountRequestDTO.getEmail())).thenReturn(LocalDateTime.now().minusMinutes(5));

        // Mock fetching the user and user info from Redis
        when(otpService.getUser(verifyAccountRequestDTO.getEmail())).thenReturn(userFromRedis);
        when(otpService.getUserInfo(verifyAccountRequestDTO.getEmail())).thenReturn(userInfoFromRedis);

        // Mock the validation service to return true
        when(validationService.validateRegister(userFromRedis.getUsername(), userFromRedis.getEmail(), userInfoFromRedis.getPhone()))
                .thenReturn(true);

        // Mock repository save actions
        when(userRepository.save(userFromRedis)).thenReturn(userFromRedis);
        when(userInfoRepository.save(userInfoFromRedis)).thenReturn(userInfoFromRedis);

        // Run the verifyAccount method
        ResponseData<?> response = authenticationUserService.verifyAccount(verifyAccountRequestDTO);

        // Assert the expected response
        assertEquals(ResponseCode.C200.getCode(), response.getStatus());
        assertEquals("Tài khoản đã được xác thực thành công", response.getMessage());
        verify(otpService).getOTP(verifyAccountRequestDTO.getEmail());
        verify(otpService).getOTPDateTime(verifyAccountRequestDTO.getEmail());
        verify(userRepository).save(userFromRedis);
        verify(userInfoRepository).save(userInfoFromRedis);
    }



    @Test
    void verifyAccount_accountAlreadyExists() {
        // Setup
        VerifyAccountRequestDTO verifyAccountRequestDTO = new VerifyAccountRequestDTO("test@example.com", "123456");

        User userFromRedis = new User();
        userFromRedis.setUsername("username");
        userFromRedis.setEmail("test@example.com");

        UserInfo userInfoFromRedis = new UserInfo();
        userInfoFromRedis.setPhone("123456789");

        // Mock the OTP service to return a valid OTP and timestamp within 10 minutes
        when(otpService.getOTP(verifyAccountRequestDTO.getEmail())).thenReturn("123456");
        when(otpService.getOTPDateTime(verifyAccountRequestDTO.getEmail())).thenReturn(LocalDateTime.now().minusMinutes(5));

        // Mock fetching the user and user info from Redis
        when(otpService.getUser(verifyAccountRequestDTO.getEmail())).thenReturn(userFromRedis);
        when(otpService.getUserInfo(verifyAccountRequestDTO.getEmail())).thenReturn(userInfoFromRedis);

        // Mock the validation service to throw DataExistedException
        doThrow(new DataExistedException("Account already exists")).when(validationService)
                .validateRegister(userFromRedis.getUsername(), userFromRedis.getEmail(), userInfoFromRedis.getPhone());

        // Run the verifyAccount method
        ResponseData<?> response = authenticationUserService.verifyAccount(verifyAccountRequestDTO);

        // Assert the expected response
        assertEquals(ResponseCode.C204.getCode(), response.getStatus());
        assertEquals("Tài khoản đã tồn tại trong hệ thống!", response.getMessage());
        verify(otpService).getOTP(verifyAccountRequestDTO.getEmail());
        verify(otpService).getOTPDateTime(verifyAccountRequestDTO.getEmail());
    }

    @Test
    void testCheckEmailExisted_NullRequest() {
        // Test when the requestDTO is null
        ResponseData<User> response = authenticationUserService.checkEmailExisted(null);

        assertEquals(ResponseCode.C205.getCode(), response.getStatus(), "Expected status code is C205");
        assertEquals("Sai request", response.getMessage(), "Expected message is 'Sai request'");
        assertNull(response.getData(), "Expected data to be null");
    }

    @Test
    void testCheckEmailExisted_UserNotFound() {
        // Prepare a mock requestDTO
        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setEmail("nonexistent@example.com");

        // Mock the userRepository to return null
        when(userRepository.findUserByEmail(requestDTO.getEmail())).thenReturn(null);

        // Call the method under test
        ResponseData<User> response = authenticationUserService.checkEmailExisted(requestDTO);

        assertEquals(ResponseCode.C206.getCode(), response.getStatus(), "Expected status code is C206");
        assertEquals("User không tồn tại trong hệ thống. Hãy đăng ký!", response.getMessage(), "Expected message is 'User không tồn tại trong hệ thống. Hãy đăng ký!'");
        assertNull(response.getData(), "Expected data to be null");
    }

    @Test
    void testCheckEmailExisted_UserFound() {
        // Prepare a mock requestDTO
        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setEmail("existent@example.com");

        // Prepare a mock User entity
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("existent@example.com");
        mockUser.setUsername("existingUser");

        // Mock the userRepository to return the mockUser
        when(userRepository.findUserByEmail(requestDTO.getEmail())).thenReturn(mockUser);

        // Call the method under test
        ResponseData<User> response = authenticationUserService.checkEmailExisted(requestDTO);

        assertEquals(ResponseCode.C200.getCode(), response.getStatus(), "Expected status code is C200");
        assertEquals("User tồn tại trong hệ thống", response.getMessage(), "Expected message is 'User tồn tại trong hệ thống'");
        assertNotNull(response.getData(), "Expected data not to be null");
        assertEquals(mockUser, response.getData(), "Expected returned user to match the mock user");
    }

    @Test
    void testCheckEmailExisted_Exception() {
        // Prepare a mock requestDTO
        EmailRequestDTO requestDTO = new EmailRequestDTO();
        requestDTO.setEmail("existent@example.com");

        // Mock the userRepository to throw an exception
        when(userRepository.findUserByEmail(requestDTO.getEmail())).thenThrow(new RuntimeException("Database error"));

        // Call the method under test
        ResponseData<User> response = authenticationUserService.checkEmailExisted(requestDTO);

        assertEquals(ResponseCode.C201.getCode(), response.getStatus(), "Expected status code is C201");
        assertEquals("Database error", response.getMessage(), "Expected message is 'Database error'");
        assertNull(response.getData(), "Expected data to be null");
    }


}
