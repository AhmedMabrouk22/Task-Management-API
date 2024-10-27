package org.example.taskmanagementapi.utils;

import org.example.taskmanagementapi.config.security.user.CustomUserDetails;
import org.example.taskmanagementapi.entities.ProjectMembers;
import org.example.taskmanagementapi.enums.ProjectRole;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.repositories.ProjectMembersRepository;
import org.example.taskmanagementapi.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProjectMemberUtils {

    private final ProjectMembersRepository projectMembersRepository;

    @Autowired
    public ProjectMemberUtils(ProjectMembersRepository projectMembersRepository) {
        this.projectMembersRepository = projectMembersRepository;
    }

    public ProjectMembers getProjectMember(long projectId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return projectMembersRepository.findByUser_Id(currentUser.getId(),projectId)
                .orElseThrow(() -> new AuthException("You unauthorized to access this project", HttpStatus.UNAUTHORIZED));
    }

    public boolean isProjectManager(ProjectMembers members) {
        return members.getRole() == ProjectRole.PROJECT_MANAGER;
    }

}
