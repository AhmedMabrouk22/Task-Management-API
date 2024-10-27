package org.example.taskmanagementapi.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.example.taskmanagementapi.annotations.ValidEnum;
import org.example.taskmanagementapi.enums.TaskPriority;
import org.example.taskmanagementapi.enums.TasksStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;


public class CreateTaskDTO {
    @NotBlank(message = "title is required")
    private String title;
    private String description;

    @JsonProperty(value = "project_id")
    @NotNull(message = "project is required")
    private long projectId;

    private LocalDateTime deadline;


    @ValidEnum(enumClass = TaskPriority.class, message = "invalid value, value must be: HIGH or MEDIUM or LOW")
    private TaskPriority priority;


    public CreateTaskDTO() {
    }



    public CreateTaskDTO(String title, String description, long projectId, LocalDateTime deadline, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.projectId = projectId;
        this.deadline = deadline;
        this.priority = priority;
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

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

}
