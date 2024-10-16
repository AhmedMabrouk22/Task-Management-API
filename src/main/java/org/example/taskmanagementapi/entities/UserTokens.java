package org.example.taskmanagementapi.entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_tokens")
public class UserTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;
    private LocalDateTime expireAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserTokens() {
    }

    public UserTokens(long id, String token, LocalDateTime expireAt, User user) {
        this.id = id;
        this.token = token;
        this.expireAt = expireAt;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
