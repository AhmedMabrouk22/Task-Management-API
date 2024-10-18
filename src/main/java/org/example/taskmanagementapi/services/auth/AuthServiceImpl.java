package org.example.taskmanagementapi.services.auth;

import jakarta.transaction.Transactional;
import org.example.taskmanagementapi.dto.OTP.UserOTPDTO;
import org.example.taskmanagementapi.dto.auth.*;
import org.example.taskmanagementapi.dto.user.UserDTO;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.entities.UserTokens;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.exceptions.auth.InvalidEmailOrPasswordException;
import org.example.taskmanagementapi.mappers.UserMapper;
import org.example.taskmanagementapi.services.OTP.UserOTPService;
import org.example.taskmanagementapi.services.token.RefreshTokenService;
import org.example.taskmanagementapi.services.user.UserService;
import org.example.taskmanagementapi.utils.EmailService;
import org.example.taskmanagementapi.utils.JwtUtils;
import org.example.taskmanagementapi.utils.OtpService;
import org.example.taskmanagementapi.utils.RefreshTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;


@Service
public class AuthServiceImpl implements AuthService{
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserOTPService userOTPService;
    private final OtpService otpService;

    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;


    @Autowired
    public AuthServiceImpl(UserService userService,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils,
                           AuthenticationManager authenticationManager,
                           UserMapper userMapper,
                           UserOTPService userOTPService,
                           OtpService otpService,
                           RefreshTokenService refreshTokenService,
                           EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.userOTPService = userOTPService;
        this.otpService = otpService;
        this.refreshTokenService = refreshTokenService;
        this.emailService = emailService;
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
        String refreshToken = RefreshTokenGenerator.generateRandomToken();

//        save refreshToken to DB
        refreshTokenService.saveToken(refreshToken,user);


        AuthResponseDTO res = new AuthResponseDTO();
        res.setAccessToken(token);
        res.setRefreshToken(refreshToken);
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
        String refreshToken = RefreshTokenGenerator.generateRandomToken();
        refreshTokenService.saveToken(refreshToken,user);

        return new AuthResponseDTO(token,refreshToken,user.getId());
    }

    @Override
    @Transactional
    public AuthResponseDTO changePassword(ChangePasswordDTO changePasswordDTO, Principal currentUser) {
//        get user from database
        User user = this.userService.findUserByEmail(currentUser.getName());
//        compare passed password with the current password
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(),user.getPassword())) {
            throw new AuthException("Invalid password", HttpStatus.BAD_REQUEST);
        }

//        if ok, change password
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        user.setLastPasswordChange(LocalDateTime.now());
        userService.saveUser(user);

        refreshTokenService.deleteAllByUserId(user.getId());

        String token = jwtUtils.generateToken(user);
        String refreshToken = RefreshTokenGenerator.generateRandomToken();
        refreshTokenService.saveToken(refreshToken,user);

        return new AuthResponseDTO(token,refreshToken,user.getId());
    }

    @Override
    @Transactional
    public void forgetPassword(ForgetPasswordDTO forgetPasswordDTO) {

//        get user form database
        User user = userService.findUserByEmail(forgetPasswordDTO.getEmail());
        if (user == null) {
            throw new AuthException("User not found", HttpStatus.NOT_FOUND);
        }
//        generate otp and send it to email
        String otp = otpService.generateOTP();
        LocalDateTime expireDate = otpService.generateExpirationTime();

//        TODO: encrypt otp and send it to email
        String encryptedOTP = otpService.encrypt(otp);
        userOTPService.saveOTP(new UserOTPDTO(encryptedOTP,expireDate,user));

//        send otp to email
        String mailMessage = "your otp code for reset password is: " + otp + " this valid will be expired after 30 minutes";
        emailService.sendEmail(user.getEmail(),"Reset password", mailMessage);

    }

    @Override
    @Transactional
    public void verifyOtp(VerifyOTPDTO verifyOTPDTO) {

//        TODO: encrypt otp
        String otp = otpService.encrypt(verifyOTPDTO.getOTP());
        verifyOTPDTO.setOTP(otp);
        userOTPService.verify(verifyOTPDTO);
    }


    @Override
    @Transactional
    public AuthResponseDTO resetPassword(ResetPasswordDTO resetPasswordDTO) {

//        Check if this user exist or not
        User user = userService.findUserByEmail(resetPasswordDTO.getEmail());

        if (user == null) {
            throw new AuthException("user not found", HttpStatus.NOT_FOUND);
        }

//        Check if otp is verified or not
         if (!userOTPService.isVerified(resetPasswordDTO.getEmail())) {
             throw new AuthException("otp not verified",HttpStatus.BAD_REQUEST);
         }

//        change password
         String newPassword = passwordEncoder.encode(resetPasswordDTO.getPassword());
         user.setPassword(newPassword);
         user.setLastPasswordChange(LocalDateTime.now());
         userService.saveUser(user);

        refreshTokenService.deleteAllByUserId(user.getId());

//        delete all otps for this user
        userOTPService.deleteAllOtp(resetPasswordDTO.getEmail());

//        generate new token
        String token = jwtUtils.generateToken(user);
        String refreshToken = RefreshTokenGenerator.generateRandomToken();
        refreshTokenService.saveToken(refreshToken,user);

        return new AuthResponseDTO(token,refreshToken,user.getId());
    }

    @Override
    public RefreshTokenDTO refreshAccessToken(RefreshTokenDTO refreshTokenDTO) {

//        check if this user have refresh token
        UserTokens token = refreshTokenService.findToken(refreshTokenDTO.getToken(),
                refreshTokenDTO.getUserId(),LocalDateTime.now());

        if (token == null) {
            throw new AuthException("token or user not found", HttpStatus.NOT_FOUND);
        }

        User user = token.getUser();
        String accessToken = jwtUtils.generateToken(user);

        return new RefreshTokenDTO(user.getId(),accessToken);


    }

    @Override
    @Transactional
    public void logout(RefreshTokenDTO refreshTokenDTO) {
        refreshTokenService.deleteToken(refreshTokenDTO.getUserId(),refreshTokenDTO.getToken());
    }


}
