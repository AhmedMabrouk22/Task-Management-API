package org.example.taskmanagementapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VerifyOTPDTO {
    @NotBlank(message = "email is required")
    @Email(message = "please enter valid email")
    private String email;

    @NotBlank(message = "OTP is required")
    @JsonProperty(value = "otp")

    private String OTP;

    public VerifyOTPDTO() {
    }

    public VerifyOTPDTO(String email, String OTP) {
        this.email = email;
        this.OTP = OTP;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }
}
