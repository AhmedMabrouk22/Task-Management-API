package org.example.taskmanagementapi.mappers;

import org.example.taskmanagementapi.dto.user.UserDTO;
import org.example.taskmanagementapi.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto (User user);
    User ToEntity(UserDTO userDTO);
}
