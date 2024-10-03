package org.example.taskmanagementapi.controllers;


import jakarta.validation.Valid;
import org.example.taskmanagementapi.dto.UserDTO;
import org.example.taskmanagementapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("signup")
    public UserDTO signup(@RequestBody @Valid UserDTO userDTO) {
        return userService.createUser(userDTO);
    }
}
