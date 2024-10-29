package org.example.taskmanagementapi.services.otp;

import org.example.taskmanagementapi.dto.auth.VerifyOTPDTO;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.entities.UserOTP;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.repositories.UserOTPRepository;
import org.example.taskmanagementapi.services.OTP.UserOTPServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserOTPServiceTest {
    @Mock
    UserOTPRepository userOTPRepository;
    @InjectMocks
    UserOTPServiceImpl userOTPService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void shouldReturnThrowIfOtpNotFound() {
        VerifyOTPDTO otpdto = new VerifyOTPDTO("test@gmail.com","123456");
        when(userOTPRepository.findOTP(anyString(),anyString(),any(LocalDateTime.class)))
                .thenThrow(new AuthException("Invalid otp or it expired", HttpStatus.BAD_REQUEST));

        AuthException ex = assertThrows(AuthException.class,() -> userOTPService.isOTPExist(otpdto));

        assertEquals("Invalid otp or it expired", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    public void shouldVerifyOtpSuccess() {
        VerifyOTPDTO otpdto = new VerifyOTPDTO("test@gmail.com","123456");
        UserOTP otp = new UserOTP("otp",LocalDateTime.now(),new User("name","email","123465"));
        when(userOTPRepository.findOTP(anyString(),anyString(),any(LocalDateTime.class)))
                .thenReturn(Optional.of(otp));

        userOTPService.verify(otpdto);

        verify(userOTPRepository,times(1)).save(any(UserOTP.class));

    }
}
