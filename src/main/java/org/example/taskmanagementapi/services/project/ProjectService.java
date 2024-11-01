package org.example.taskmanagementapi.services.project;

import org.example.taskmanagementapi.dto.project.CreateProjectDTO;
import org.example.taskmanagementapi.dto.project.ProjectMembersPageResponseDTO;
import org.example.taskmanagementapi.dto.project.ProjectPageResponseDTO;
import org.example.taskmanagementapi.dto.project.ProjectResponseDTO;
import org.example.taskmanagementapi.entities.Project;

public interface ProjectService {
    ProjectResponseDTO save(CreateProjectDTO projectDTO);
    Project findProjectById(long id);
    ProjectResponseDTO findProjectDTOById(long id);
    ProjectResponseDTO updateById(long id, CreateProjectDTO projectDTO);
    void deleteById(long project_id);

    ProjectPageResponseDTO getAllProjects(int page, int size);

    void addProjectMember(String userEmail, long projectId);
    void deleteProjectMember(long userId,long projectId);

    ProjectMembersPageResponseDTO getProjectMembers(long projectId, int page, int size);

}
