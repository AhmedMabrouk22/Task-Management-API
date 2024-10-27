package org.example.taskmanagementapi.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.taskmanagementapi.annotations.ValidEnum;
import org.example.taskmanagementapi.dto.project.ProjectMemberDTO;
import org.example.taskmanagementapi.enums.TaskPriority;
import org.example.taskmanagementapi.enums.TasksStatus;

import java.time.LocalDateTime;

public class TaskDTO {
    private long id;
    private String title;
    private String description;
    private LocalDateTime deadline;

    private TasksStatus status;
    private TaskPriority priority;

    private LocalDateTime createdAt;

    @JsonProperty(value = "assign_to")
    private ProjectMemberDTO assignTo;

    public TaskDTO(long id, String title, String description, LocalDateTime deadline, TasksStatus status, TaskPriority priority, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public TasksStatus getStatus() {
        return status;
    }

    public void setStatus(TasksStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public ProjectMemberDTO getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(ProjectMemberDTO assignTo) {
        this.assignTo = assignTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
