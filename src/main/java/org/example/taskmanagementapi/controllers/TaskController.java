package org.example.taskmanagementapi.controllers;


import jakarta.validation.Valid;
import org.example.taskmanagementapi.dto.task.CreateTaskDTO;
import org.example.taskmanagementapi.dto.task.UpdateTaskDTO;
import org.example.taskmanagementapi.services.task.TaskService;
import org.example.taskmanagementapi.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTask(@RequestBody @Valid CreateTaskDTO taskDTO) {
        var data = taskService.create(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.response(true,"task created successfully", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getTasks(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(required = false) String[] sort,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(required = false) String priority,
                                                @RequestParam(required = false) Long assignTo) {
        var data = taskService.getAllTasks(page,size,sort,status,priority,assignTo);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true, "Tasks get successfully", data));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable long taskId) {
        taskService.delete(taskId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"task with id: " + taskId + " deleted successfully", null));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<ApiResponse> updateTask(@PathVariable long taskId, @RequestBody @Valid UpdateTaskDTO updateTaskDTO) {
        var data = taskService.update(taskId,updateTaskDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"Task with id: " + taskId + " updated successfully", data));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse> getTaskById(@PathVariable long taskId) {
        var data = taskService.getTaskById(taskId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"Task with id: " + taskId + " get successfully", data));
    }

}
