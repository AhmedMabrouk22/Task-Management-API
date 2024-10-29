package org.example.taskmanagementapi.services.OTP;

import jakarta.transaction.Transactional;
import org.example.taskmanagementapi.dto.OTP.UserOTPDTO;
import org.example.taskmanagementapi.dto.auth.VerifyOTPDTO;
import org.example.taskmanagementapi.entities.UserOTP;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.repositories.UserOTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserOTPServiceImpl implements UserOTPService{
    private final UserOTPRepository userOTPRepository;
    @Autowired
    public UserOTPServiceImpl(UserOTPRepository userOTPRepository) {
        this.userOTPRepository = userOTPRepository;
    }

    public UserOTP isOTPExist(VerifyOTPDTO verifyOTPDTO) {
        LocalDateTime currentDate = LocalDateTime.now();
        return userOTPRepository.findOTP(verifyOTPDTO.getOTP(),verifyOTPDTO.getEmail(),currentDate)
                .orElseThrow(()-> new AuthException("Invalid otp or it expired", HttpStatus.BAD_REQUEST));
    }

    @Override
    @Transactional
    public void saveOTP(UserOTPDTO otp) {
        UserOTP userOTP = new UserOTP(
                otp.getOtp(),
                otp.getExpireDate(),
                otp.getUser()
        );
        userOTPRepository.save(userOTP);
    }

    @Override
    @Transactional
    public void verify(VerifyOTPDTO verifyOTPDTO) {
        UserOTP otp = isOTPExist(verifyOTPDTO);
        otp.setVerified(true);
        userOTPRepository.save(otp);
    }

    @Override
    public boolean isVerified(String email) {
        UserOTP otp = userOTPRepository.findUserOTPSByUser_EmailAndVerifiedIsTrue(email);
        return otp != null;
    }

    @Override
    public void deleteAllOtp(String email) {
        userOTPRepository.deleteAllByUser_Email(email);
    }
}
