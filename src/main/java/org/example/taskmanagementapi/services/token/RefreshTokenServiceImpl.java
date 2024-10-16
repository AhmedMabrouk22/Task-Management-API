package org.example.taskmanagementapi.services.token;

import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.entities.UserTokens;
import org.example.taskmanagementapi.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void saveToken(String token, User user) {
        UserTokens refreshToken = new UserTokens();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpireAt(LocalDateTime.now().plusDays(30));
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void deleteAllByUserId(long id) {
        refreshTokenRepository.deleteAllByUser_Id(id);
    }

    @Override
    public UserTokens findToken(String token, long user_id, LocalDateTime date) {
        return refreshTokenRepository.findUserTokensByTokenAndUser_IdAndExpireAtIsAfter(token,user_id,date);
    }

    @Override
    public void deleteToken(long user_id, String token) {
        refreshTokenRepository.deleteByTokenAndUser_Id(token,user_id);
    }
}
