package org.example.taskmanagementapi.services.project_members;

import org.example.taskmanagementapi.entities.ProjectMembers;

public interface ProjectMembersService {
    ProjectMembers getProjectMember(long projectId);
    boolean isProjectManager(ProjectMembers members);
}
