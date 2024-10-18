package org.example.taskmanagementapi.controllers;


import jakarta.validation.Valid;
import org.example.taskmanagementapi.dto.auth.*;
import org.example.taskmanagementapi.services.auth.AuthService;
import org.example.taskmanagementapi.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody @Valid SignupRequestDTO signupDTO) {
        var data =  authService.signup(signupDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.response(true,"User signup successfully", data));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        var data = authService.login(loginDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true, "User login successfully", data));
    }

    @PostMapping("change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO,
                                                      Principal currentUser) {
        var data = authService.changePassword(changePasswordDTO,currentUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"Password changed successfully",data));
    }

    @PostMapping("forget-password")
    public ResponseEntity<ApiResponse> forgetPassword(@RequestBody @Valid ForgetPasswordDTO forgetPasswordDTO) {
        authService.forgetPassword(forgetPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"OTP send successfully to your email",null));
    }

    @PostMapping("verify-otp")
    public ResponseEntity<ApiResponse> verifyOTP(@RequestBody @Valid VerifyOTPDTO verifyOTPDTO) {
        authService.verifyOtp(verifyOTPDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"OTP verified",null));
    }

    @PostMapping("reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        var data = authService.resetPassword(resetPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"password change successfully",data));
    }

    @PostMapping("refresh-access-token")
    public ResponseEntity<ApiResponse> refreshAccessToken(@RequestBody @Valid RefreshTokenDTO refreshTokenDTO) {
        var data = authService.refreshAccessToken(refreshTokenDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.response(true,"Access token created successfully",data));
    }

    @PostMapping("logout")
    public ResponseEntity<ApiResponse> logout(@RequestBody @Valid RefreshTokenDTO refreshTokenDTO) {
        authService.logout(refreshTokenDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"user logout",null));
    }

}
