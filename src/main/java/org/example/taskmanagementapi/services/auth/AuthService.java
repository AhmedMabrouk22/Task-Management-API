package org.example.taskmanagementapi.services.auth;

import org.example.taskmanagementapi.dto.auth.*;
import org.example.taskmanagementapi.entities.User;
import org.springframework.stereotype.Service;

import java.security.Principal;

public interface AuthService {
    User getLoggedUser();
    AuthResponseDTO signup(SignupRequestDTO signupRequestDTO);
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
    AuthResponseDTO changePassword(ChangePasswordDTO changePasswordDTO);
    void forgetPassword(ForgetPasswordDTO forgetPasswordDTO);
    void verifyOtp(VerifyOTPDTO verifyOTPDTO);
    AuthResponseDTO resetPassword(ResetPasswordDTO resetPasswordDTO);

    RefreshTokenDTO refreshAccessToken(RefreshTokenDTO refreshTokenDTO);
    void logout(RefreshTokenDTO refreshTokenDTO);

}
