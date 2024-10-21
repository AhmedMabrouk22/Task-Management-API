package org.example.taskmanagementapi.dto.project;

import org.example.taskmanagementapi.dto.PageResponseDTO;

import java.util.List;

public class ProjectPageResponseDTO extends PageResponseDTO {
    private List<ProjectResponseDTO> projects;

    public ProjectPageResponseDTO(List<ProjectResponseDTO> projects, int totalPages, long totalElements, int currentPage) {
        super(totalPages,totalElements,currentPage);
        this.projects = projects;
    }

    public List<ProjectResponseDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectResponseDTO> projects) {
        this.projects = projects;
    }

}
