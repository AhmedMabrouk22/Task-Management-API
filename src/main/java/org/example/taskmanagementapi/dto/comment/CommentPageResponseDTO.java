package org.example.taskmanagementapi.dto.comment;

import org.example.taskmanagementapi.dto.PageResponseDTO;

import java.util.List;

public class CommentPageResponseDTO extends PageResponseDTO {

    List<CommentDTO> comments;

    public CommentPageResponseDTO(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public CommentPageResponseDTO(int totalPages, long totalElements, int currentPage, List<CommentDTO> comments) {
        super(totalPages, totalElements, currentPage);
        this.comments = comments;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}
