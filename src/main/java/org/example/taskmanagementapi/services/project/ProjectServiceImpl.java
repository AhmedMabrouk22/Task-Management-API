package org.example.taskmanagementapi.services.project;


import jakarta.transaction.Transactional;
import org.example.taskmanagementapi.config.security.user.CustomUserDetails;
import org.example.taskmanagementapi.dto.project.*;
import org.example.taskmanagementapi.entities.Project;
import org.example.taskmanagementapi.entities.ProjectMembers;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.enums.ProjectRole;
import org.example.taskmanagementapi.exceptions.DatabaseException;
import org.example.taskmanagementapi.exceptions.NotFoundExceptionHandler;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.repositories.ProjectMembersRepository;
import org.example.taskmanagementapi.repositories.ProjectRepository;
import org.example.taskmanagementapi.services.user.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final Environment environment;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              UserService userService,
                              ProjectMembersRepository projectMembersRepository, Environment environment) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.projectMembersRepository = projectMembersRepository;
        this.environment = environment;
    }

    private ProjectMembers getTeamMember(long project_id) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectMembersRepository.findByUser_Id(currentUser.getId(),project_id)
                .orElseThrow(() -> new AuthException("You unauthorized to access this project", HttpStatus.UNAUTHORIZED));
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

    private ProjectMemberDTO buildProjectMemberResponse(ProjectMembers member) {
        User user = member.getUser();
        return new ProjectMemberDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getImage(),
                member.getRole()
        );
    }


    @Override
    public Project findProjectById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundExceptionHandler("Project with ID " + id + " not found"));
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
    public ProjectResponseDTO findProjectDTOById(long id) {
        Project project =  findProjectById(id);
        getTeamMember(id);

        String sizeENV = environment.getProperty("project.members.size");
        int size = 10;
        if (sizeENV != null) size = Integer.parseInt(sizeENV);
        ProjectMembersPageResponseDTO members = getProjectMembers(id,0,size);

//        System.out.println("Project Members: ");
//        project.getTeamMembers().forEach(member -> {
//            System.out.println("name: " + member.getUser().getName());
//            System.out.println("image: " + member.getUser().getImage());
//            System.out.println("email: " + member.getUser().getEmail());
//        });
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                members,
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
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
        try {
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
        catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("user with email: " + user_email + " already member in this project");
        }
    }

    @Override
    @Transactional
    public void deleteTeamMember(long user_id, long project_id) {
        var projectManager = getTeamMember(project_id);
        if (projectManager.getRole() != ProjectRole.PROJECT_MANAGER) {
            throw new AuthException("You Unauthorized to delete member from this project", HttpStatus.UNAUTHORIZED);
        }
        projectMembersRepository.deleteByUser_IdAndProject_Id(user_id,project_id);
    }

    @Override
    public ProjectMembersPageResponseDTO getProjectMembers(long project_id, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<ProjectMembers> members = projectMembersRepository.findByProject_Id(project_id,pageable);
        List<ProjectMemberDTO> projectMember = members.stream()
                .map(this::buildProjectMemberResponse)
                .toList();

        return new ProjectMembersPageResponseDTO(
                members.getTotalPages(),
                members.getTotalElements(),
                members.getNumber(),
                projectMember
        );
    }


}

