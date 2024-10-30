package org.example.taskmanagementapi.services.project_members;

import org.example.taskmanagementapi.config.security.user.CustomUserDetails;
import org.example.taskmanagementapi.entities.ProjectMembers;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.enums.ProjectRole;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.repositories.ProjectMembersRepository;
import org.example.taskmanagementapi.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProjectMemberServiceImpl implements ProjectMembersService {

    private final ProjectMembersRepository projectMembersRepository;
    private final AuthService authService;

    @Autowired
    public ProjectMemberServiceImpl(ProjectMembersRepository projectMembersRepository, AuthService authService) {
        this.projectMembersRepository = projectMembersRepository;
        this.authService = authService;
    }


    @Override
    public ProjectMembers getProjectMember(long projectId) {
        User currentUser = authService.getLoggedUser();
        return projectMembersRepository.findByUser_Id(currentUser.getId(),projectId)
                .orElseThrow(() -> new AuthException("You unauthorized to access this project", HttpStatus.UNAUTHORIZED));
    }
    @Override
    public boolean isProjectManager(ProjectMembers members) {
        return members.getRole() == ProjectRole.PROJECT_MANAGER;
    }

}
