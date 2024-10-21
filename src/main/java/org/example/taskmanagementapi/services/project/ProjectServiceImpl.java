package org.example.taskmanagementapi.services.project;


import jakarta.transaction.Transactional;
import org.example.taskmanagementapi.config.security.user.CustomUserDetails;
import org.example.taskmanagementapi.dto.project.CreateProjectDTO;
import org.example.taskmanagementapi.dto.project.ProjectPageResponseDTO;
import org.example.taskmanagementapi.dto.project.ProjectResponseDTO;
import org.example.taskmanagementapi.entities.Project;
import org.example.taskmanagementapi.entities.ProjectMembers;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.enums.ProjectRole;
import org.example.taskmanagementapi.exceptions.NotFoundExceptionHandler;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.repositories.ProjectMembersRepository;
import org.example.taskmanagementapi.repositories.ProjectRepository;
import org.example.taskmanagementapi.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ProjectMembersRepository projectMembersRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              UserService userService,
                              ProjectMembersRepository projectMembersRepository) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.projectMembersRepository = projectMembersRepository;
    }

    private Project findProjectById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundExceptionHandler("Project with ID " + id + " not found"));
    }

    private ProjectMembers getTeamMember(long project_id) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectMembersRepository.findByUser_Id(currentUser.getId(),project_id)
                .orElseThrow(() -> new AuthException("You Unauthorized to update this project", HttpStatus.UNAUTHORIZED));
    }

    private ProjectResponseDTO buildProjectResponse(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }


    @Override
    @Transactional
    public ProjectResponseDTO save(CreateProjectDTO projectDTO, Principal currentUser) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());

        User user = userService.findUserByEmail(currentUser.getName());
        ProjectMembers members = new ProjectMembers();
        members.setUser(user);
        members.setProject(project);
        members.setRole(ProjectRole.PROJECT_MANAGER);

        projectMembersRepository.save(members);
        project = projectRepository.save(project);
        return buildProjectResponse(project);

    }

    @Override
    public ProjectResponseDTO findById(long id) {
        Project project =  findProjectById(id);
        getTeamMember(id);
        return buildProjectResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponseDTO updateById(long id, CreateProjectDTO projectDTO) {
        Project project =  findProjectById(id);
        // Check if the user is member in this project and the role is PROJECT_MANAGER
        var member = getTeamMember(id);
        if ( member.getRole() != ProjectRole.PROJECT_MANAGER) {
            throw new AuthException("You Unauthorized to update this project", HttpStatus.UNAUTHORIZED);
        }

        if (projectDTO.getName() != null)
                project.setName(projectDTO.getName());
        if (projectDTO.getDescription() != null)
            project.setDescription(projectDTO.getDescription());

        project.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(project);
        return buildProjectResponse(project);
    }

    @Override
    @Transactional
    public void deleteById(long project_id) {
        findProjectById(project_id);
        ProjectMembers member = getTeamMember(project_id);
        if ( member.getRole() != ProjectRole.PROJECT_MANAGER) {
            throw new AuthException("You Unauthorized to delete this project", HttpStatus.UNAUTHORIZED);
        }

        projectRepository.deleteById(project_id);

    }

    @Override
    public ProjectPageResponseDTO getAllProjects(int page, int size) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projects = projectRepository.findAllByTeamMembers_UserId(currentUser.getId(),pageable);
        List<ProjectResponseDTO> projectResponses = projects.stream()
                .map(this::buildProjectResponse)
                .toList();

        return new ProjectPageResponseDTO(
                projectResponses,
                projects.getTotalPages(),
                projects.getTotalElements(),
                projects.getNumber()
        );

    }

    @Override
    @Transactional
    public void addTeamMember(String user_email,long project_id) {
        var projectManager = getTeamMember(project_id);
        if (projectManager.getRole() != ProjectRole.PROJECT_MANAGER) {
            throw new AuthException("You Unauthorized to add member to this project", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findUserByEmail(user_email);
        Project project = findProjectById(project_id);
        ProjectMembers member = new ProjectMembers();
        member.setRole(ProjectRole.TEAM_MEMBER);
        member.setUser(user);
        member.setProject(project);
        projectMembersRepository.save(member);
    }

    @Override
    public void deleteTeamMember(long user_id, long project_id) {
        var projectManager = getTeamMember(project_id);
        if (projectManager.getRole() != ProjectRole.PROJECT_MANAGER) {
            throw new AuthException("You Unauthorized to delete member from this project", HttpStatus.UNAUTHORIZED);
        }

        projectMembersRepository.deleteByUser_IdAndProject_Id(user_id,project_id);
    }


}

