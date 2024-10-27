package org.example.taskmanagementapi.dto.task;

import org.example.taskmanagementapi.dto.PageResponseDTO;

import java.util.List;

public class TaskPageResponseDTO extends PageResponseDTO {
    private List<TaskDTO> tasks;

    public TaskPageResponseDTO(int totalPages, long totalElements, int currentPage, List<TaskDTO> tasks) {
        super(totalPages, totalElements, currentPage);
        this.tasks = tasks;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }
}
