package org.example.taskmanagementapi.services.comment;

import org.example.taskmanagementapi.dto.comment.AddCommentDTO;
import org.example.taskmanagementapi.dto.comment.CommentDTO;
import org.example.taskmanagementapi.dto.comment.CommentPageResponseDTO;
import org.example.taskmanagementapi.entities.Comment;

public interface CommentService {

    Comment findCommentById(Long commentId);
    CommentDTO add(AddCommentDTO commentDTO);
    CommentDTO update(Long commentId, AddCommentDTO commentDTO);
    void delete(Long commentId);

    CommentPageResponseDTO getComments(Long taskId, int page, int size);

}
