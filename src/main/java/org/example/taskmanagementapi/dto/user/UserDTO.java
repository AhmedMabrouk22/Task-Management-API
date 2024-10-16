package org.example.taskmanagementapi.dto.user;

public class UserDTO {
    private long id;
    private String name;
    private String password;
    private String image;

    public UserDTO() {
    }

    public UserDTO(long id, String name, String password, String image) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.image = image;
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
