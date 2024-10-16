package org.example.taskmanagementapi.repositories;

import org.example.taskmanagementapi.entities.UserOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserOTPRepository extends JpaRepository<UserOTP,Long> {
    UserOTP findUserOTPSByOtpAndExpireDateIsAfterAndUser_EmailAndVerifiedIsFalse(String otp, LocalDateTime expireDate, String user_email);
    UserOTP findUserOTPSByUser_EmailAndVerifiedIsTrue(String email);
    void deleteAllByUser_Email(String email);
}
