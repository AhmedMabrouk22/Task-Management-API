package org.example.taskmanagementapi.dto.auth;

public class AuthResponseDTO {
    private String token;
    private long userId;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, long userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
