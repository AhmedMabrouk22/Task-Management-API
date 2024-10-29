package org.example.taskmanagementapi.services.auth;

import org.example.taskmanagementapi.dto.OTP.UserOTPDTO;
import org.example.taskmanagementapi.dto.auth.AuthResponseDTO;
import org.example.taskmanagementapi.dto.auth.ForgetPasswordDTO;
import org.example.taskmanagementapi.dto.auth.ResetPasswordDTO;
import org.example.taskmanagementapi.dto.auth.SignupRequestDTO;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.mappers.UserMapper;
import org.example.taskmanagementapi.services.OTP.UserOTPService;
import org.example.taskmanagementapi.services.token.RefreshTokenService;
import org.example.taskmanagementapi.services.user.UserService;
import org.example.taskmanagementapi.utils.EmailService;
import org.example.taskmanagementapi.utils.JwtUtils;
import org.example.taskmanagementapi.utils.OtpService;
import org.example.taskmanagementapi.utils.RefreshTokenGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserOTPService userOTPService;
    @Mock
    private OtpService otpService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthServiceImpl authService;
    private SignupRequestDTO signupRequestDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setName("Test User");
        signupRequestDTO.setEmail("test@example.com");
        signupRequestDTO.setPassword("password123");

    }
    @Test
    public void shouldThrowExceptionWhenSignupIfUserAlreadyExists() {
        when(userService.existsUserByEmail(signupRequestDTO.getEmail())).thenReturn(true);
        AuthException exception = assertThrows(AuthException.class, () -> authService.signup(signupRequestDTO));
        assertEquals("This user already exist", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    public void shouldSignupNewUserSuccessfully() {
        // Arrange
        User mockUser = new User(signupRequestDTO.getName(),signupRequestDTO.getEmail(),"hashed_password");
        mockUser.setId(1);
        when(userService.existsUserByEmail(signupRequestDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequestDTO.getPassword())).thenReturn("hashed_password");
        when(userService.saveUser(any(User.class))).thenReturn(mockUser);
        when(jwtUtils.generateToken(any(User.class))).thenReturn("access_token");
        MockedStatic<RefreshTokenGenerator> mockedStatic = Mockito.mockStatic(RefreshTokenGenerator.class);;
        mockedStatic.when(RefreshTokenGenerator::generateRandomToken).thenReturn("refresh_token");
        doNothing().when(refreshTokenService).saveToken(anyString(), any(User.class));

        // Act
        AuthResponseDTO res = authService.signup(signupRequestDTO);

        // Assert
        assertEquals("access_token",res.getAccessToken());
        assertEquals("refresh_token",res.getRefreshToken());
        mockedStatic.close();
    }

    @Test
    public void shouldReturnExceptionUserNotFoundWhenForgetPassword() {

        ForgetPasswordDTO forgetPasswordDTO = new ForgetPasswordDTO("test@gmail.com");
        when(userService.findUserByEmail(forgetPasswordDTO.getEmail())).thenReturn(null);
        AuthException ex = assertThrows(AuthException.class, ()-> authService.forgetPassword(forgetPasswordDTO));
        verify(otpService, never()).generateOTP();
        verify(otpService, never()).generateExpirationTime();
        verify(otpService, never()).encrypt(anyString());
        verify(userOTPService, never()).saveOTP(any(UserOTPDTO.class));
        verify(emailService, never()).sendEmail(anyString(),anyString(),anyString());

        assertEquals("User not found", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND,ex.getStatusCode());

    }

    @Test
    public void shouldSendOTPSuccessfully() {
        User mokeUser = new User("test","test@email.com","123456798");
        String otp = "123456";
        String emailSubject = "Reset password";
        String emailBody = "your otp code for reset password is: " + otp + " this valid will be expired after 30 minutes";
        ForgetPasswordDTO forgetPasswordDTO = new ForgetPasswordDTO(mokeUser.getEmail());
        when(userService.findUserByEmail(forgetPasswordDTO.getEmail())).thenReturn(mokeUser);
        when(otpService.generateOTP()).thenReturn(otp);
        when(otpService.encrypt(otp)).thenReturn("otp");
        doNothing().when(userOTPService).saveOTP(any(UserOTPDTO.class));
        doNothing().when(emailService).sendEmail(anyString(),anyString(),anyString());

        authService.forgetPassword(forgetPasswordDTO);

        verify(userService,times(1)).findUserByEmail(mokeUser.getEmail());
        verify(userOTPService,times(1)).saveOTP(any(UserOTPDTO.class));
        verify(otpService,times(1)).encrypt(otp);
        verify(emailService,times(1)).sendEmail(mokeUser.getEmail(),emailSubject,emailBody);
    }

    @Test
    public void shouldResetPasswordSuccessfully() {

        User mockUser = new User("test","test@gmail.com","123456789");
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
        resetPasswordDTO.setEmail("test@gmail.com");
        resetPasswordDTO.setPassword("123456789");

        when(userService.findUserByEmail(mockUser.getEmail())).thenReturn(mockUser);
        when(userOTPService.isVerified(mockUser.getEmail())).thenReturn(true);
        when(passwordEncoder.encode("123456789")).thenReturn("hashed_password");
        when(userService.saveUser(any(User.class))).thenReturn(mockUser);
        doNothing().when(refreshTokenService).deleteAllByUserId(1);
        when(jwtUtils.generateToken(any(User.class))).thenReturn("access_token");
        MockedStatic<RefreshTokenGenerator> mockedStatic = Mockito.mockStatic(RefreshTokenGenerator.class);
        mockedStatic.when(RefreshTokenGenerator::generateRandomToken).thenReturn("refresh_token");
        doNothing().when(refreshTokenService).saveToken(anyString(), any(User.class));
        doNothing().when(userOTPService).deleteAllOtp(resetPasswordDTO.getEmail());

        AuthResponseDTO res = authService.resetPassword(resetPasswordDTO);

        verify(refreshTokenService,times(1)).deleteAllByUserId(anyLong());
        verify(userOTPService,times(1)).deleteAllOtp(resetPasswordDTO.getEmail());
        assertEquals("access_token",res.getAccessToken());
        assertEquals("refresh_token",res.getRefreshToken());
        mockedStatic.close();

    }
    @Test
    public void shouldThrowErrorIfOTPNotVerifiedWhenResetPassword() {
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
        resetPasswordDTO.setEmail("test@gmail.com");
        resetPasswordDTO.setPassword("123456789");

        when(userService.findUserByEmail(resetPasswordDTO.getEmail()))
                .thenReturn( new User("test","test@gmail.com","123456789"));
        when(userOTPService.isVerified(resetPasswordDTO.getEmail())).thenReturn(false);

        AuthException ex = assertThrows(AuthException.class, () -> authService.resetPassword(resetPasswordDTO));

        assertEquals("Otp not verified", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,ex.getStatusCode());
        verify(refreshTokenService,never()).deleteAllByUserId(anyLong());
        verify(userOTPService,never()).deleteAllOtp(resetPasswordDTO.getEmail());

    }

}