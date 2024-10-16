package org.example.taskmanagementapi.repositories;

import org.example.taskmanagementapi.entities.UserTokens;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface RefreshTokenRepository extends JpaRepository<UserTokens,Long> {
    UserTokens findUserTokensByTokenAndUser_IdAndExpireAtIsAfter(String token, long user_id, LocalDateTime expireAt);
    void deleteAllByUser_Id(long id);

    @Query("delete from UserTokens where token = :token and user.id = :user_id")
    @Modifying
    void deleteByTokenAndUser_Id(String token, long user_id);

}
