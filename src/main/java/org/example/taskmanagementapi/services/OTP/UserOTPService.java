package org.example.taskmanagementapi.services.OTP;

import org.example.taskmanagementapi.dto.OTP.UserOTPDTO;
import org.example.taskmanagementapi.dto.auth.VerifyOTPDTO;

public interface UserOTPService {
    void saveOTP(UserOTPDTO otp);
    void verify(VerifyOTPDTO verifyOTPDTO);
    boolean isVerified(String email);
    void deleteAllOtp(String email);

}
