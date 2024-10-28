package org.example.taskmanagementapi.mappers;

import org.example.taskmanagementapi.dto.comment.CommentDTO;
import org.example.taskmanagementapi.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user", target = "author")
    CommentDTO toDTO(Comment comment);
}
