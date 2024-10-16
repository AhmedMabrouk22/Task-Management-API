package org.example.taskmanagementapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDTO {

    @NotBlank(message = "Current password is required")
    @JsonProperty("current_password")
    private String currentPassword;

    @NotBlank(message = "Current password is require")
    @Size(min = 8, message = "password must be at lease 8 characters")
    @JsonProperty("new_password")
    private String newPassword;

    public ChangePasswordDTO() {
    }

    public ChangePasswordDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
