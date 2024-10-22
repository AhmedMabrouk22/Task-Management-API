package org.example.taskmanagementapi.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectResponseDTO extends CreateProjectDTO{
    private long id;
    private ProjectMembersPageResponseDTO projectMembers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public ProjectResponseDTO(long id, String name, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(name, description);
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ProjectResponseDTO(long id, String name, String description, ProjectMembersPageResponseDTO projectMembers, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this(id, name, description, createdAt, updatedAt);
        this.projectMembers = projectMembers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public ProjectMembersPageResponseDTO getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(ProjectMembersPageResponseDTO projectMembers) {
        this.projectMembers = projectMembers;
    }
}
