package org.example.taskmanagementapi.mappers;

import org.example.taskmanagementapi.dto.task.TaskDTO;
import org.example.taskmanagementapi.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "user", target = "assignTo")
    TaskDTO toDto(Task task);
    Task toEntity(TaskDTO taskDTO);
}
