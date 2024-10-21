package org.example.taskmanagementapi.services.project;

import org.example.taskmanagementapi.dto.project.CreateProjectDTO;
import org.example.taskmanagementapi.dto.project.ProjectMembersPageResponseDTO;
import org.example.taskmanagementapi.dto.project.ProjectPageResponseDTO;
import org.example.taskmanagementapi.dto.project.ProjectResponseDTO;
import org.example.taskmanagementapi.entities.Project;

import java.security.Principal;
import java.util.List;

public interface ProjectService {
    ProjectResponseDTO save(CreateProjectDTO projectDTO, Principal currentUser);
    ProjectResponseDTO findById(long id);
    ProjectResponseDTO updateById(long id, CreateProjectDTO projectDTO);
    void deleteById(long project_id);

    ProjectPageResponseDTO getAllProjects(int page, int size);
    void addTeamMember(String user_email, long project_id);
    void deleteTeamMember(long user_id,long project_id);

    ProjectMembersPageResponseDTO getProjectMembers(long project_id, int page, int size);

}
