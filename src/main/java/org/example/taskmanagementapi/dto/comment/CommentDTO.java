package org.example.taskmanagementapi.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.taskmanagementapi.dto.user.UserDTO;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDTO {
    private long id;
    private String content;
    @JsonIgnoreProperties({"lastPasswordChange"})
    private UserDTO author;
    private List<MentionListResponseDTO> mentions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public List<MentionListResponseDTO> getMentions() {
        return mentions;
    }

    public void setMentions(List<MentionListResponseDTO> mentions) {
        this.mentions = mentions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
