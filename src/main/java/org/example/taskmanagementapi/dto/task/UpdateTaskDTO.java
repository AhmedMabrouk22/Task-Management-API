package org.example.taskmanagementapi.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.taskmanagementapi.annotations.ValidEnum;
import org.example.taskmanagementapi.dto.project.ProjectMemberDTO;
import org.example.taskmanagementapi.enums.TaskPriority;
import org.example.taskmanagementapi.enums.TasksStatus;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"projectId"})
public class UpdateTaskDTO{

    private String title;
    private String description;
    private LocalDateTime deadline;

    @ValidEnum(enumClass = TaskPriority.class, message = "invalid value, value must be: HIGH or MEDIUM or LOW")
    private TaskPriority priority;

    @ValidEnum(enumClass = TasksStatus.class, message = "invalid value, status must be: TO_DO, IN_PROGRESS or COMPLETED")
    private TasksStatus status;
    @JsonProperty(value = "assign_to")
    private long assignToId;


    public long getAssignToId() {
        return this.assignToId;
    }

    public void setAssignToId(long assignTo) {
        this.assignToId = assignTo;
    }

    public TasksStatus getStatus() {
        return status;
    }

    public void setStatus(TasksStatus status) {
        this.status = status;
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
}
