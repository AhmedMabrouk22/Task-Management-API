package org.example.taskmanagementapi.dto.user;

import java.time.LocalDateTime;

public class UserDTO {
    private long id;
    private String name;
    private String email;
    private String image;

    private LocalDateTime lastPasswordChange;
    public UserDTO() {
    }

    public UserDTO(long id, String name, String email, String image, LocalDateTime lastPasswordChange) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.lastPasswordChange = lastPasswordChange;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(LocalDateTime lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }
}
