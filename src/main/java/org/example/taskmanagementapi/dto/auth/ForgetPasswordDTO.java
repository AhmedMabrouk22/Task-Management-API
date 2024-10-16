package org.example.taskmanagementapi.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgetPasswordDTO {

    @NotBlank(message = "email is required")
    @Email(message = "please enter valid email")
    private String email;

    public ForgetPasswordDTO() {
    }

    public ForgetPasswordDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
