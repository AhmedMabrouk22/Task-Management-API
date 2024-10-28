package org.example.taskmanagementapi.services.project_members;

import org.example.taskmanagementapi.entities.ProjectMembers;

public interface ProjectMembersService {

    /**
     * check if the logged user is a member if specific project and return project member object
     * if the user not member in the project it will throw error
     * @param projectId
     * @return ProjectMembers
     */
    ProjectMembers getProjectMember(long projectId);
    boolean isProjectManager(ProjectMembers members);
}