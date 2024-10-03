package org.example.taskmanagementapi.dto;


import jakarta.validation.constraints.*;

public class UserDTO {
    private int id;

    @NotBlank(message = "User must have a name")
    private String name;

    @Email(message = "Please add valid email")
    private String email;

    @Size(min = 8, message = "Password must have at least 8 character")
    private String password;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
