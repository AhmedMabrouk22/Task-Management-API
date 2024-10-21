package org.example.taskmanagementapi.config.security.user;

import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public interface CustomUserDetails extends UserDetails {

    long getId();

    LocalDateTime getLastPasswordChange();
}
