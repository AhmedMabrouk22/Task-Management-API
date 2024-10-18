package org.example.taskmanagementapi.services.token;

import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.entities.UserTokens;

import java.time.LocalDateTime;

public interface RefreshTokenService {
    void saveToken(String token, User user);
    void deleteAllByUserId(long id);
    UserTokens findToken(String token,long user_id,LocalDateTime date);
    void deleteToken(long user_id,String token);


}
