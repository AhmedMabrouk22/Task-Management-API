package org.example.taskmanagementapi.controllers;


import jakarta.validation.Valid;
import org.example.taskmanagementapi.dto.auth.AuthResponseDTO;
import org.example.taskmanagementapi.dto.auth.LoginRequestDTO;
import org.example.taskmanagementapi.dto.auth.SignupRequestDTO;
import org.example.taskmanagementapi.services.auth.AuthService;
import org.example.taskmanagementapi.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody @Valid SignupRequestDTO signupDTO) {
        var data =  authService.signup(signupDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.response(true,"User signup successfully", data));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        var data = authService.login(loginDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true, "User login successfully", data));
    }
}
