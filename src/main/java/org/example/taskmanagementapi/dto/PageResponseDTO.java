package org.example.taskmanagementapi.dto;

public class PageResponseDTO {
    private int totalPages;
    private long totalElements;
    private int currentPage;

    public PageResponseDTO() {
    }

    public PageResponseDTO(int totalPages, long totalElements, int currentPage) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
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
