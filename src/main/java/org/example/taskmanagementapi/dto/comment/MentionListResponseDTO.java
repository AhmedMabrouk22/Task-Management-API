package org.example.taskmanagementapi.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.taskmanagementapi.dto.user.UserDTO;

public class MentionListResponseDTO {
    private long index;

    @JsonIgnoreProperties({"lastPasswordChange"})
    private UserDTO user;

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
