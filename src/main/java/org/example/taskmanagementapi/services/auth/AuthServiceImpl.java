package org.example.taskmanagementapi.services.auth;

import jakarta.transaction.Transactional;
import org.example.taskmanagementapi.dto.auth.AuthResponseDTO;
import org.example.taskmanagementapi.dto.auth.LoginRequestDTO;
import org.example.taskmanagementapi.dto.auth.SignupRequestDTO;
import org.example.taskmanagementapi.dto.user.UserDTO;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.exceptions.auth.InvalidEmailOrPasswordException;
import org.example.taskmanagementapi.mappers.UserMapper;
import org.example.taskmanagementapi.services.user.UserService;
import org.example.taskmanagementapi.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService{
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(UserService userService,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils,
                           AuthenticationManager authenticationManager,
                           UserMapper userMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public AuthResponseDTO signup(SignupRequestDTO signupRequestDTO) {
        // Check if the email exist
        if (userService.existsUserByEmail(signupRequestDTO.getEmail())) {
            throw new AuthException("This user already exist", HttpStatus.BAD_REQUEST);
        }

        String password = passwordEncoder.encode(signupRequestDTO.getPassword());

        // Create new user
        User user = new User(
                signupRequestDTO.getName(),
                signupRequestDTO.getEmail(),
                password
        );

        userService.saveUser(user);

        // Generate token
        String token = jwtUtils.generateToken(user);

        AuthResponseDTO res = new AuthResponseDTO();
        res.setToken(token);
        res.setUserId(user.getId());

        return res;
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),loginRequestDTO.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new InvalidEmailOrPasswordException();
        }

        UserDTO userDto = userService.findUserDTOByEmail(loginRequestDTO.getEmail());
        User user = userMapper.ToEntity(userDto);

        String token = jwtUtils.generateToken(user);

        return new AuthResponseDTO(token,user.getId());
    }


}
