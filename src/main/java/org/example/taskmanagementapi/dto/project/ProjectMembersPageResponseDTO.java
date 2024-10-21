package org.example.taskmanagementapi.dto.project;

import org.example.taskmanagementapi.dto.PageResponseDTO;

import java.util.List;

public class ProjectMembersPageResponseDTO extends PageResponseDTO {
    private List<ProjectMemberDTO> members;

    public ProjectMembersPageResponseDTO(List<ProjectMemberDTO> members) {
        this.members = members;
    }
    public ProjectMembersPageResponseDTO(int totalPages, long totalElements, int currentPage, List<ProjectMemberDTO> members) {
        super(totalPages, totalElements, currentPage);
        this.members = members;
    }

    public List<ProjectMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<ProjectMemberDTO> members) {
        this.members = members;
    }
}
