package org.example.taskmanagementapi.services.project_members;

import jakarta.transaction.Transactional;
import org.example.taskmanagementapi.dto.project.ProjectMemberDTO;
import org.example.taskmanagementapi.dto.project.ProjectMembersPageResponseDTO;
import org.example.taskmanagementapi.entities.Project;
import org.example.taskmanagementapi.entities.ProjectMembers;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.enums.ProjectRole;
import org.example.taskmanagementapi.exceptions.DatabaseException;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.repositories.ProjectMembersRepository;
import org.example.taskmanagementapi.services.auth.AuthService;
import org.example.taskmanagementapi.services.project.ProjectService;
import org.example.taskmanagementapi.services.user.UserService;
import org.example.taskmanagementapi.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class ProjectMemberServiceImpl implements ProjectMembersService {

    private final ProjectMembersRepository projectMembersRepository;
    private final AuthService authService;

    @Autowired
    public ProjectMemberServiceImpl(ProjectMembersRepository projectMembersRepository,
                                    AuthService authService) {
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
    public boolean isNotProjectManager(ProjectMembers members) {
        return members.getRole() != ProjectRole.PROJECT_MANAGER;
    }

    @Override
    @Transactional
    public ProjectMembers addMember(ProjectMembers member) {
        return projectMembersRepository.save(member);
    }

    @Override
    @Transactional
    public void deleteMember(long userId, long projectId) {
        projectMembersRepository.deleteByUser_IdAndProject_Id(userId,projectId);
    }

    @Override
    public ProjectMembersPageResponseDTO getMembers(long projectId, int page, int size) {
        getProjectMember(projectId);
        Pageable pageable = PageRequest.of(page,size);
        Page<ProjectMemberDTO> members = projectMembersRepository.findByProject_Id(projectId,pageable);
        List<ProjectMemberDTO> projectMember = PageUtils.convertPage(members, Function.identity());

        return new ProjectMembersPageResponseDTO(
                members.getTotalPages(),
                members.getTotalElements(),
                members.getNumber(),
                projectMember
        );
    }

}
