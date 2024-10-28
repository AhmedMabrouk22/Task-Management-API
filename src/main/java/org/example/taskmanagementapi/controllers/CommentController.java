package org.example.taskmanagementapi.controllers;


import jakarta.validation.Valid;
import org.example.taskmanagementapi.dto.comment.AddCommentDTO;
import org.example.taskmanagementapi.services.comment.CommentService;
import org.example.taskmanagementapi.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> addComment(@RequestBody @Valid AddCommentDTO commentDTO) {
        var data = commentService.add(commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.response(true,"commend added successfully", data));
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse> getComments(@PathVariable Long taskId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        var data = commentService.getComments(taskId,page,size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"comments get successfully", data));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable Long commentId,
                                                     @RequestBody @Valid AddCommentDTO commentDTO) {
        var data = commentService.update(commentId,commentDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"comments updated successfully", data));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"comments deleted successfully", null));
    }


}
