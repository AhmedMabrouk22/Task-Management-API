package org.example.taskmanagementapi.dto.project;

import java.util.List;

public class ProjectPageResponseDTO {
    private List<ProjectResponseDTO> projects;
    private int totalPages;
    private long totalElements;
    private int currentPage;

    public ProjectPageResponseDTO(List<ProjectResponseDTO> projects, int totalPages, long totalElements, int currentPage) {
        this.projects = projects;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
    }

    public List<ProjectResponseDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectResponseDTO> projects) {
        this.projects = projects;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
