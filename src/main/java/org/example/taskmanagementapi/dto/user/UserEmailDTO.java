package org.example.taskmanagementapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserEmailDTO {

    @NotBlank(message = "email is required")
    @Email(message = "invalid email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
