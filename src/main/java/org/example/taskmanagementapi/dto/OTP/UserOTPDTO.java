package org.example.taskmanagementapi.dto.OTP;

import org.example.taskmanagementapi.entities.User;

import java.time.LocalDateTime;

public class UserOTPDTO {
    private String otp;
    private LocalDateTime expireDate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

    public UserOTPDTO(String otp, LocalDateTime expireDate, User user) {
        this.otp = otp;
        this.expireDate = expireDate;
        this.user = user;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

}
