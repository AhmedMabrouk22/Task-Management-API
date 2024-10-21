package org.example.taskmanagementapi.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.taskmanagementapi.dto.project.CreateProjectDTO;
import org.example.taskmanagementapi.services.project.ProjectService;
import org.example.taskmanagementapi.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/projects")
@Validated
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @RequestMapping
    public ResponseEntity<ApiResponse> createProject(@RequestBody @Valid CreateProjectDTO projectDTO, Principal currentUser) {
        var data = projectService.save(projectDTO, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.response(true,"project created successfully", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> findAllProjects(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        var data = projectService.getAllProjects(page,size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true, "Project get successfully", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findProjectById(@PathVariable long id) {
        var data = projectService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"project get successfully", data));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProjectById(@PathVariable long id,
                                                         @RequestBody @Valid CreateProjectDTO projectDTO) {
        var data = projectService.updateById(id,projectDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"Project with id: " + id + " updated successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProjectById(@PathVariable long id) {
        projectService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"Project with id: " + id + " deleted successfully",null));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<ApiResponse> addTeamMember(@RequestBody
                                                         @NotBlank(message = "email is required")
                                                         @Email(message = "invalid email") String email,
                                                     @PathVariable long id) {
        projectService.addTeamMember(email,id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"member add successfully to project with id: " + id, null));
    }
    
    @DeleteMapping("/{id}/members")
    public ResponseEntity<ApiResponse> deleteTeamMember(@RequestBody()
                                                         @NotNull(message = "user id is required") long user_id,
                                                     @PathVariable long id) {
        projectService.deleteTeamMember(user_id,id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"member deleted successfully to project with id: " + id, null));
    } 
}
