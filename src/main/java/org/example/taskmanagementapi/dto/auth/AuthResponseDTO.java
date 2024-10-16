package org.example.taskmanagementapi.dto.auth;

public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private long userId;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String accessToken,String refreshToken, long userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }



}
