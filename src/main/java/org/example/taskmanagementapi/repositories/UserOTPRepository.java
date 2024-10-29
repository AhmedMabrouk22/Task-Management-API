package org.example.taskmanagementapi.repositories;

import org.example.taskmanagementapi.entities.UserOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserOTPRepository extends JpaRepository<UserOTP,Long> {

    @Query(
            "select t from UserOTP t where t.otp =:otp and t.user.email =:userEmail and t.expireDate > :currentDate"
    )
    Optional<UserOTP> findOTP(String otp, String userEmail, LocalDateTime currentDate);

//    UserOTP findUserOTPSByOtpAndExpireDateIsAfterAndUser_EmailAndVerifiedIsFalse(String otp, LocalDateTime expireDate, String userEmail);
    UserOTP findUserOTPSByUser_EmailAndVerifiedIsTrue(String email);
    void deleteAllByUser_Email(String email);
}
