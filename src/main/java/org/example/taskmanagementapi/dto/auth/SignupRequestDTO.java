package org.example.taskmanagementapi.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequestDTO {

    @NotBlank(message = "user name is required")
    private String name;

    @NotBlank(message = "user email is required")
    @Email(message = "please enter valid email")
    private String email;

    @NotBlank(message = "user password is required")
    @Size(min = 8, message = "password must be at lease 8 characters")
    private String password;

    public SignupRequestDTO() {
    }

    public SignupRequestDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
