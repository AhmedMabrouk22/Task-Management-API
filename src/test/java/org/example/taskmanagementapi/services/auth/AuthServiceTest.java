package org.example.taskmanagementapi.services.auth;

import org.example.taskmanagementapi.dto.auth.AuthResponseDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public void shouldThrowExceptionIfUserAlreadyExists() {
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
        MockedStatic<RefreshTokenGenerator> mockedStatic = Mockito.mockStatic(RefreshTokenGenerator.class);
        mockedStatic.when(RefreshTokenGenerator::generateRandomToken).thenReturn("refresh_token");
        doNothing().when(refreshTokenService).saveToken(anyString(), any(User.class));

        // Act
        AuthResponseDTO res = authService.signup(signupRequestDTO);

        // Assert
        assertEquals("access_token",res.getAccessToken());
        assertEquals("refresh_token",res.getRefreshToken());
    }


}