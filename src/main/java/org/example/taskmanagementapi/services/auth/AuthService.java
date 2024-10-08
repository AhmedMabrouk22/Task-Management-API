package org.example.taskmanagementapi.services.auth;

import org.example.taskmanagementapi.dto.auth.AuthResponseDTO;
import org.example.taskmanagementapi.dto.auth.LoginRequestDTO;
import org.example.taskmanagementapi.dto.auth.SignupRequestDTO;
import org.springframework.stereotype.Service;

public interface AuthService {
    AuthResponseDTO signup(SignupRequestDTO signupRequestDTO);
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
}
