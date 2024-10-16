package org.example.taskmanagementapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenDTO {
    @JsonProperty(value = "user_id")
    private long userId;

    private String token;

    public RefreshTokenDTO() {
    }

    public RefreshTokenDTO(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
