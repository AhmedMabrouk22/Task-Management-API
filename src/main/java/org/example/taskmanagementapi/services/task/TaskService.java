package org.example.taskmanagementapi.services.task;

import org.example.taskmanagementapi.dto.task.CreateTaskDTO;
import org.example.taskmanagementapi.dto.task.TaskDTO;
import org.example.taskmanagementapi.dto.task.TaskPageResponseDTO;
import org.example.taskmanagementapi.dto.task.UpdateTaskDTO;
import org.example.taskmanagementapi.entities.Task;

public interface TaskService {
    TaskDTO create(CreateTaskDTO taskDTO);

    Task findTaskById(long taskId);
    void delete(long taskId);
    TaskDTO update(long taskId, UpdateTaskDTO taskDTO);
    TaskDTO getTaskById(long taskId);

    TaskPageResponseDTO getAllTasks(int page,int size,
                                    String[] sort,
                                    String status,
                                    String priority,
                                    Long assignTo);

}
