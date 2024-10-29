package org.example.taskmanagementapi.controllers;


import org.example.taskmanagementapi.services.user.UserService;
import org.example.taskmanagementapi.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("me")
    public ResponseEntity<ApiResponse> getLoggedUser() {
        var data = userService.getLoggedUser();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"logged user get successfully", data));
    }
}
