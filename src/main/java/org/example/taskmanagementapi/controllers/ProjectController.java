package org.example.taskmanagementapi.controllers;

import jakarta.validation.Valid;
import org.example.taskmanagementapi.dto.project.CreateProjectDTO;
import org.example.taskmanagementapi.dto.user.UserEmailDTO;
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
        var data = projectService.findProjectDTOById(id);
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
    public ResponseEntity<ApiResponse> addTeamMember(@RequestBody @Valid UserEmailDTO email,
                                                     @PathVariable long id) {
        projectService.addTeamMember(email.getEmail(),id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"member add successfully to project with id: " + id, null));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<ApiResponse> getProjectMembers(@PathVariable long id,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        var data = projectService.getProjectMembers(id,page,size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"project members get successfully", data));
    }

    @DeleteMapping("/{project_id}/members/{member_id}")
    public ResponseEntity<ApiResponse> deleteTeamMember(@PathVariable long project_id, @PathVariable long member_id) {
        projectService.deleteTeamMember(member_id,project_id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.response(true,"member deleted successfully to project with id: " + project_id, null));
    } 
}
